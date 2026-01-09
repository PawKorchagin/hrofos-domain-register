--liquibase formatted sql

--changeset system:forbidden_name-001-create-table
CREATE TABLE forbidden_name (
    id    uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    label text NOT NULL UNIQUE
);
