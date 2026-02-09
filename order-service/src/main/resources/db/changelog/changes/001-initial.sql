--liquibase formatted sql
--changeset order-service:001-initial
CREATE TABLE IF NOT EXISTS "order" (
    id              BIGSERIAL PRIMARY KEY,
    status          VARCHAR(50) NOT NULL,
    created_at      TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
