--liquibase formatted sql

--changeset system:auth-factor-001-create-enum
-- Create auth_factor_kind enum type
DO $$ BEGIN
    CREATE TYPE auth_factor_kind AS ENUM ('TOTP', 'WebAuthn');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

--changeset system:auth-factor-002-create-table
-- Create auth_factor table
CREATE TABLE IF NOT EXISTS auth_factor (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
    kind auth_factor_kind NOT NULL,
    public_data JSONB NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create index on user_id for faster lookups
CREATE INDEX IF NOT EXISTS idx_auth_factor_user_id ON auth_factor(user_id);

-- Create trigger to automatically update updated_at
CREATE TRIGGER trigger_auth_factor_updated_at
    BEFORE UPDATE ON auth_factor
    FOR EACH ROW
    EXECUTE FUNCTION set_updated_at();
