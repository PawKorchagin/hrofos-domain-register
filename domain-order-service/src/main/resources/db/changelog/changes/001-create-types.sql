--liquibase formatted sql

--changeset system:001-create-types-item-action
CREATE TYPE item_action AS ENUM ('register','renew');

--changeset system:001-create-types-item-term
CREATE TYPE item_term AS ENUM ('monthly','yearly');

--changeset system:001-create-types-order-status
CREATE TYPE order_status AS ENUM ('created','pending_payment','paid','cancelled','failed');

--changeset system:001-create-types-domain-member-role
CREATE TYPE domain_member_role AS ENUM ('OWNER','USER');
