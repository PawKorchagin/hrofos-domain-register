--liquibase formatted sql
--changeset domain-service:006-create-domain-reservation
CREATE TABLE domain_reservation (
    id              BIGSERIAL PRIMARY KEY,
    payment_id     UUID NOT NULL,
    user_id        UUID NOT NULL,
    l3_domain      TEXT NOT NULL,
    period         VARCHAR(16) NOT NULL,
    expires_at     TIMESTAMP NOT NULL,
    created_at     TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE(payment_id, l3_domain)
);

CREATE INDEX idx_reservation_payment_id ON domain_reservation(payment_id);
CREATE INDEX idx_reservation_user_id ON domain_reservation(user_id);
CREATE INDEX idx_reservation_expires_at ON domain_reservation(expires_at);
CREATE INDEX idx_reservation_l3_domain ON domain_reservation(l3_domain);
