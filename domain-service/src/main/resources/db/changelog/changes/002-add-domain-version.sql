--liquibase formatted sql
--changeset domain-service:002-add-domain-version
ALTER TABLE domain ADD COLUMN domain_version BIGINT;
