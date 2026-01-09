--liquibase formatted sql

--changeset system:reserved_name-001-create-table
CREATE TABLE reserved_name (
    id         uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    label      text NOT NULL UNIQUE,
    reason     text,
    created_at timestamptz NOT NULL DEFAULT now(),
    updated_at timestamptz NOT NULL DEFAULT now()
);
