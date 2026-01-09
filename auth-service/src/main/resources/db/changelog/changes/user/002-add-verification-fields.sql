-- Add email verification fields to app_user table
ALTER TABLE app_user 
ADD COLUMN IF NOT EXISTS email_verified BOOLEAN NOT NULL DEFAULT false,
ADD COLUMN IF NOT EXISTS verification_token UUID DEFAULT gen_random_uuid();

-- Create index on verification_token for faster lookups
CREATE INDEX IF NOT EXISTS idx_app_user_verification_token ON app_user(verification_token);
