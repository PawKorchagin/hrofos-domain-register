--liquibase formatted sql

--changeset system:domain_expiry_monitoring-001-create-table
CREATE TABLE domain_expiry_monitoring (
    id          uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id     uuid NOT NULL,
    domain_id   uuid NOT NULL,
    alert_days  integer NOT NULL CHECK (alert_days >${DB_USER:***REMOVED***} 0),
    notified_at timestamptz,
    created_at  timestamptz NOT NULL DEFAULT now(),
    updated_at  timestamptz NOT NULL DEFAULT now(),
    UNIQUE (user_id, domain_id, alert_days)
);
