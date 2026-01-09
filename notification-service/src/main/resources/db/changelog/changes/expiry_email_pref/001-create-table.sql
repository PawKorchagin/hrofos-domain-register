--liquibase formatted sql

--changeset system:expiry_email_pref-001-create-table
CREATE TABLE expiry_email_pref (
    id          uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id     uuid NOT NULL,
    days_before integer NOT NULL CHECK (days_before >${DB_USER:***REMOVED***} 0),
    enabled     boolean NOT NULL DEFAULT true,
    created_at  timestamptz NOT NULL DEFAULT now(),
    updated_at  timestamptz NOT NULL DEFAULT now(),
    UNIQUE (user_id, days_before)
);
