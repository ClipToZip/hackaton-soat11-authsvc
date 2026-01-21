CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS clicktozip.user (
    user_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name varchar(255),
    email varchar(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_app_user_email ON clicktozip.user(email);