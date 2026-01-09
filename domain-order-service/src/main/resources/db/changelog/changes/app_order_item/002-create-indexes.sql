--liquibase formatted sql

--changeset system:app_order_item-002-create-indexes
CREATE INDEX app_order_item_order_id_idx ON app_order_item(order_id);
