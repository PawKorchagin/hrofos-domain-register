--liquibase formatted sql

--changeset system:app_order-001-create-table
CREATE TABLE app_order (
    id           uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id      uuid NOT NULL,
    status       order_status NOT NULL DEFAULT 'created',
    total_amount bigint NOT NULL CHECK (total_amount >${DB_USER:***REMOVED***} 0),
    created_at   timestamptz NOT NULL DEFAULT now(),
    paid_at      timestamptz,
    updated_at   timestamptz NOT NULL DEFAULT now()
);
