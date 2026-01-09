--liquibase formatted sql

--changeset system:user-004-rename-admin-to-is-admin
-- Rename admin column to is_admin
ALTER TABLE app_user
RENAME COLUMN admin TO is_admin;
