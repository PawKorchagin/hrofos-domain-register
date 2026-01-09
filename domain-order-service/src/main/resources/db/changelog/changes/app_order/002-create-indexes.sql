--liquibase formatted sql

--changeset system:app_order-002-create-indexes-user-id
CREATE INDEX app_order_user_id_idx ON app_order(user_id);

--changeset system:app_order-002-create-indexes-created-paid
CREATE INDEX app_order_created_at_paid_at_idx ON app_order(created_at, paid_at) WHERE paid_at IS NULL;
