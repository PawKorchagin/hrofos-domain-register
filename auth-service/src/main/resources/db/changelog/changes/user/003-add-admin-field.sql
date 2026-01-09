--liquibase formatted sql

--changeset system:user-003-add-admin-field
-- Add admin field to app_user table
ALTER TABLE app_user
ADD COLUMN IF NOT EXISTS admin BOOLEAN NOT NULL DEFAULT false;
