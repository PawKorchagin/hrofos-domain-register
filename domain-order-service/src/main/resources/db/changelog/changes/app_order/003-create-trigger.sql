--liquibase formatted sql

--changeset system:app_order-003-create-trigger
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.triggers WHERE trigger_name ${DB_USER:***REMOVED***} 'app_order_set_updated_at'
CREATE TRIGGER app_order_set_updated_at
    BEFORE UPDATE ON app_order
    FOR EACH ROW
    EXECUTE FUNCTION set_updated_at();
