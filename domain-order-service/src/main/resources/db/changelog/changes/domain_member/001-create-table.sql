--liquibase formatted sql

--changeset system:domain_member-001-create-table
CREATE TABLE domain_member (
    id         uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id    uuid NOT NULL,
    domain_id  uuid NOT NULL REFERENCES domain(id) ON DELETE CASCADE,
    role       domain_member_role NOT NULL,
    created_at timestamptz NOT NULL DEFAULT now(),
    UNIQUE (user_id, domain_id)
);
