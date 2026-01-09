--liquibase formatted sql

--changeset system:cart_item-002-create-trigger
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.triggers WHERE trigger_name ${DB_USER:***REMOVED***} 'cart_item_set_updated_at'
CREATE TRIGGER cart_item_set_updated_at
    BEFORE UPDATE ON cart_item
    FOR EACH ROW
    EXECUTE FUNCTION set_updated_at();
