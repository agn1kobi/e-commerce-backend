CREATE TABLE IF NOT EXISTS orders (
        id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
        total_price NUMERIC(19,4) NOT NULL,
        created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
        updated_at TIMESTAMPTZ,
        deleted_at TIMESTAMPTZ
);

-- Index for reporting on order creation time
CREATE INDEX IF NOT EXISTS idx_orders_created_at ON orders(created_at);
