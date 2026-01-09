--liquibase formatted sql

--changeset system:domain-003-create-trigger
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.triggers WHERE trigger_name ${DB_USER:***REMOVED***} 'domain_set_updated_at'
CREATE TRIGGER domain_set_updated_at
    BEFORE UPDATE ON domain
    FOR EACH ROW
    EXECUTE FUNCTION set_updated_at();
