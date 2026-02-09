--liquibase formatted sql
--changeset payment-service:001-initial
CREATE TABLE payment (
    id               UUID PRIMARY KEY,
    user_id          UUID NOT NULL,
    period           VARCHAR(16) NOT NULL,
    amount           INTEGER NOT NULL,
    currency         VARCHAR(8) NOT NULL,
    status           VARCHAR(32) NOT NULL,
    operation_id     VARCHAR(128),
    payment_url      VARCHAR(1024),
    operation_status VARCHAR(64),
    domains_created  BOOLEAN NOT NULL DEFAULT FALSE,
    created_at       TIMESTAMP NOT NULL,
    updated_at       TIMESTAMP,
    paid_at          TIMESTAMP
);

CREATE TABLE payment_domains (
    payment_id  UUID NOT NULL,
    l3_domain   VARCHAR(255) NOT NULL,
    PRIMARY KEY (payment_id, l3_domain),
    CONSTRAINT fk_payment_domains_payment
        FOREIGN KEY (payment_id)
        REFERENCES payment (id)
        ON DELETE CASCADE
);

CREATE INDEX idx_payment_user_id ON payment (user_id);
CREATE INDEX idx_payment_operation_id ON payment (operation_id);
