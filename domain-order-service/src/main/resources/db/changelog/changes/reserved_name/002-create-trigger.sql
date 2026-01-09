--liquibase formatted sql

--changeset system:reserved_name-002-create-trigger
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.triggers WHERE trigger_name ${DB_USER:***REMOVED***} 'reserved_name_set_updated_at'
CREATE TRIGGER reserved_name_set_updated_at
    BEFORE UPDATE ON reserved_name
    FOR EACH ROW
    EXECUTE FUNCTION set_updated_at();
