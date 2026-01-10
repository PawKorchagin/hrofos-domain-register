--liquibase formatted sql

--changeset system:dns_record-001-create-table
CREATE TABLE dns_record (
    id         uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    domain_id  uuid NOT NULL REFERENCES domain(id) ON DELETE CASCADE,
    type       domain_record_type NOT NULL,
    name       varchar(255) NOT NULL,
    value      varchar(1024) NOT NULL,
    ttl        integer NOT NULL CHECK (ttl > 0),
    created_at timestamptz NOT NULL DEFAULT now(),
    updated_at timestamptz NOT NULL DEFAULT now()
);
