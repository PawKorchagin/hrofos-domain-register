--liquibase formatted sql

--changeset system:user-005-remove-first-last-name
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM information_schema.columns WHERE table_name ${DB_USER:***REMOVED***} 'app_user' AND column_name ${DB_USER:***REMOVED***} 'first_name'
ALTER TABLE app_user DROP COLUMN IF EXISTS first_name;

--changeset system:user-005-remove-last-name
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM information_schema.columns WHERE table_name ${DB_USER:***REMOVED***} 'app_user' AND column_name ${DB_USER:***REMOVED***} 'last_name'
ALTER TABLE app_user DROP COLUMN IF EXISTS last_name;
