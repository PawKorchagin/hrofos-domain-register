--liquibase formatted sql

--changeset system:zone2-002-create-trigger
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.triggers WHERE trigger_name ${DB_USER:***REMOVED***} 'zone2_set_updated_at'
CREATE TRIGGER zone2_set_updated_at
    BEFORE UPDATE ON zone2
    FOR EACH ROW
    EXECUTE FUNCTION set_updated_at();
