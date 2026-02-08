--liquibase formatted sql
--changeset domain-service:001-create-domain-and-dns-record
CREATE TABLE domain (
    id              BIGSERIAL PRIMARY KEY,
    domain_part     TEXT NOT NULL,
    parent_id       BIGINT
        REFERENCES domain(id) ON DELETE CASCADE
);

CREATE INDEX domain_part_idx ON domain(domain_part);

CREATE TABLE dns_record (
    id              BIGSERIAL PRIMARY KEY,
    record_data     JSONB,
    domain_id       BIGINT REFERENCES domain(id) ON DELETE CASCADE
);
