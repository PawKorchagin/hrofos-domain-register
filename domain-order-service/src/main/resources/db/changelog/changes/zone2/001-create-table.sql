--liquibase formatted sql

--changeset system:zone2-001-create-table
CREATE TABLE zone2 (
    id         uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    name       text NOT NULL UNIQUE,
    price      bigint NOT NULL CHECK (price >${DB_USER:***REMOVED***} 0),
    created_at timestamptz NOT NULL DEFAULT now(),
    updated_at timestamptz NOT NULL DEFAULT now()
);
