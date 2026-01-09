--liquibase formatted sql

--changeset system:expiry_email_pref-003-create-trigger
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.triggers WHERE trigger_name ${DB_USER:***REMOVED***} 'expiry_email_pref_set_updated_at'
CREATE TRIGGER expiry_email_pref_set_updated_at
    BEFORE UPDATE ON expiry_email_pref
    FOR EACH ROW
    EXECUTE FUNCTION set_updated_at();
