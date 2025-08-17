-- Create users table
-- Using Postgres gen_random_uuid() from pgcrypto (preferred over uuid-ossp in modern setups)
CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL
);

CREATE UNIQUE INDEX IF NOT EXISTS uk_users_email ON users(email);
