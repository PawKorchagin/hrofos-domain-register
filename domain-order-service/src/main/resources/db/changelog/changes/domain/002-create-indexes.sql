--liquibase formatted sql

--changeset system:domain-002-create-indexes
CREATE INDEX domain_expires_at_idx ON domain(expires_at);
