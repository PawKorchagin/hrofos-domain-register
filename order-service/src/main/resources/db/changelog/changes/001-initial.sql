--liquibase formatted sql
--changeset order-service:001-initial
CREATE TABLE cart (
    user_id     UUID NOT NULL,
    l3_domain   VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_id, l3_domain)
);
