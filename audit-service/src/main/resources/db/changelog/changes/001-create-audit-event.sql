--liquibase formatted sql
--changeset audit-service:001-create-audit-event
CREATE TABLE audit_event (
    id          BIGSERIAL PRIMARY KEY,
    description TEXT NOT NULL,
    user_id     UUID,
    event_time  TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX audit_event_user_id_idx ON audit_event(user_id);
CREATE INDEX audit_event_event_time_idx ON audit_event(event_time);
