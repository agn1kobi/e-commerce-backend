-- Create order_lines table with audit columns and indexes
CREATE TABLE IF NOT EXISTS order_lines (
        id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
        order_id UUID NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
        product_id UUID NOT NULL,
        product_name VARCHAR(255) NOT NULL,
        quantity INTEGER NOT NULL,
        unit_price NUMERIC(19,4) NOT NULL,
        tax_pct NUMERIC(9,4) NOT NULL,
        line_total NUMERIC(19,4) NOT NULL,
        created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
        updated_at TIMESTAMPTZ,
        deleted_at TIMESTAMPTZ
);

-- Trigger to auto-update updated_at for order_lines
DO $$ BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_trigger WHERE tgname = 'trg_order_lines_updated_at'
    ) THEN
        CREATE TRIGGER trg_order_lines_updated_at
        BEFORE UPDATE ON order_lines
        FOR EACH ROW
        EXECUTE FUNCTION set_updated_at();
    END IF;
END $$;

CREATE INDEX IF NOT EXISTS idx_order_lines_order_id ON order_lines(order_id);
CREATE INDEX IF NOT EXISTS idx_order_lines_product_id ON order_lines(product_id);
