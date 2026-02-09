--liquibase formatted sql
--changeset domain-service:005-add-activated-finished-at
ALTER TABLE domain ADD COLUMN activated_at TIMESTAMP;
ALTER TABLE domain ADD COLUMN finished_at TIMESTAMP;
