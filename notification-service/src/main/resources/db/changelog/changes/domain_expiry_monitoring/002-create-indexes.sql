--liquibase formatted sql

--changeset system:domain_expiry_monitoring-002-create-indexes
CREATE INDEX domain_expiry_monitoring_user_id_idx ON domain_expiry_monitoring(user_id);
CREATE INDEX domain_expiry_monitoring_domain_id_idx ON domain_expiry_monitoring(domain_id);
CREATE INDEX domain_expiry_monitoring_notified_at_idx ON domain_expiry_monitoring(notified_at) WHERE notified_at IS NULL;
