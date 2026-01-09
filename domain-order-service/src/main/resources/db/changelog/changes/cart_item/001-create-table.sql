--liquibase formatted sql

--changeset system:cart_item-001-create-table
CREATE TABLE cart_item (
    id         uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    cart_id    uuid NOT NULL REFERENCES cart(id) ON DELETE CASCADE,
    action     item_action NOT NULL,
    term       item_term NOT NULL,
    fqdn       text NOT NULL,
    price      bigint NOT NULL CHECK (price >${DB_USER:***REMOVED***} 0),
    created_at timestamptz NOT NULL DEFAULT now(),
    updated_at timestamptz NOT NULL DEFAULT now(),
    UNIQUE (cart_id, fqdn, action, term)
);
