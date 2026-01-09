--liquibase formatted sql

--changeset system:app_order_item-001-create-table
CREATE TABLE app_order_item (
    id         uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id   uuid NOT NULL REFERENCES app_order(id) ON DELETE CASCADE,
    action     item_action NOT NULL,
    term       item_term NOT NULL,
    domain_id  uuid NOT NULL REFERENCES domain(id) ON DELETE RESTRICT,
    created_at timestamptz NOT NULL DEFAULT now(),
    updated_at timestamptz NOT NULL DEFAULT now()
);
