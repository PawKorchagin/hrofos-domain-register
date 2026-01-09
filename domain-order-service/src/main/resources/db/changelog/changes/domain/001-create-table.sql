--liquibase formatted sql

--changeset system:domain-001-create-table
CREATE TABLE domain (
    id           uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    fqdn         varchar(63) NOT NULL UNIQUE,
    zone2_id     uuid NOT NULL REFERENCES zone2(id) ON DELETE RESTRICT,
    activated_at timestamptz,
    expires_at   timestamptz NOT NULL,
    created_at   timestamptz NOT NULL DEFAULT now(),
    updated_at   timestamptz NOT NULL DEFAULT now()
);
