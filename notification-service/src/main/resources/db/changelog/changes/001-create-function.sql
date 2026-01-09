--liquibase formatted sql

--changeset system:001-create-function
CREATE OR REPLACE FUNCTION set_updated_at() RETURNS trigger AS '
BEGIN
    NEW.updated_at :${DB_USER:***REMOVED***} now();
    RETURN NEW;
END;
' LANGUAGE plpgsql;
