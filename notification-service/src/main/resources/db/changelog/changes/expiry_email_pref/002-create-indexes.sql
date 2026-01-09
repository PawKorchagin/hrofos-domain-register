--liquibase formatted sql

--changeset system:expiry_email_pref-002-create-indexes
CREATE INDEX expiry_email_pref_user_id_idx ON expiry_email_pref(user_id);
