--liquibase formatted sql

--changeset system:dns_record-003-create-trigger
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.triggers WHERE trigger_name ${DB_USER:***REMOVED***} 'dns_record_set_updated_at'
CREATE TRIGGER dns_record_set_updated_at
    BEFORE UPDATE ON dns_record
    FOR EACH ROW
    EXECUTE FUNCTION set_updated_at();
