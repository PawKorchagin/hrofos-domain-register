--liquibase formatted sql

--changeset system:ns_delegation-002-create-trigger
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.triggers WHERE trigger_name ${DB_USER:***REMOVED***} 'ns_delegation_set_updated_at'
CREATE TRIGGER ns_delegation_set_updated_at
    BEFORE UPDATE ON ns_delegation
    FOR EACH ROW
    EXECUTE FUNCTION set_updated_at();
