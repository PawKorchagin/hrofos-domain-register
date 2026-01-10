--liquibase formatted sql

--changeset system:ns_delegation-001-create-table
CREATE TABLE ns_delegation (
    id         uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    domain_id  uuid NOT NULL UNIQUE REFERENCES domain(id) ON DELETE CASCADE,
    ns_servers jsonb NOT NULL,
    applied_at timestamptz,
    created_at timestamptz NOT NULL DEFAULT now(),
    updated_at timestamptz NOT NULL DEFAULT now()
);
