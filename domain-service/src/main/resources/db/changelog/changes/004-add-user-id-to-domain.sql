--liquibase formatted sql
--changeset domain-service:004-add-user-id-to-domain
ALTER TABLE domain ADD COLUMN user_id UUID;

CREATE INDEX domain_user_id_idx ON domain(user_id);
