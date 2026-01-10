--liquibase formatted sql

--changeset system:dns_record-002-create-indexes
CREATE INDEX dns_record_domain_id_idx ON dns_record(domain_id);
