-- Create products table with audit columns
CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE IF NOT EXISTS products (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    product_name VARCHAR(255) NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 0,
    price REAL NOT NULL DEFAULT 0,
    tax REAL NOT NULL DEFAULT 0,
    description VARCHAR(255),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ,
    deleted_at TIMESTAMPTZ
);

-- Unique constraint on product_name as per @UniqueConstraint in entity
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'uk_on_product_name'
    ) THEN
        ALTER TABLE products ADD CONSTRAINT uk_on_product_name UNIQUE (product_name);
    END IF;
END $$;

-- Ensure non-negative values at DB level
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'chk_products_quantity_non_negative'
    ) THEN
        ALTER TABLE products
            ADD CONSTRAINT chk_products_quantity_non_negative CHECK (quantity >= 0);
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'chk_products_price_non_negative'
    ) THEN
        ALTER TABLE products
            ADD CONSTRAINT chk_products_price_non_negative CHECK (price >= 0);
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'chk_products_tax_non_negative'
    ) THEN
        ALTER TABLE products
            ADD CONSTRAINT chk_products_tax_non_negative CHECK (tax >= 0);
    END IF;
END $$;

-- Trigger to auto-update updated_at for products
DO $$ BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_trigger WHERE tgname = 'trg_products_updated_at'
    ) THEN
        CREATE TRIGGER trg_products_updated_at
        BEFORE UPDATE ON products
        FOR EACH ROW
        EXECUTE FUNCTION set_updated_at();
    END IF;
END $$;
