DROP ALIAS IF EXISTS xpcasTestProcedureInt;
DROP ALIAS IF EXISTS xpcasTestProcedureString;
DROP ALIAS IF EXISTS xpcasTestProcedureBoolean;
DROP ALIAS IF EXISTS xpcasTestProcedureBoolean2;
DROP ALIAS IF EXISTS xpcasTestProcedureInstant;
DROP ALIAS IF EXISTS xpcasTestProcedureInstant2;

CREATE ALIAS xpcasTestProcedureInt FOR "aero.minova.cas.controller.SQLProcedureControllerTest.testInt";
CREATE ALIAS xpcasTestProcedureString FOR "aero.minova.cas.controller.SQLProcedureControllerTest.testString";
CREATE ALIAS xpcasTestProcedureBoolean FOR "aero.minova.cas.controller.SQLProcedureControllerTest.testBoolean";
CREATE ALIAS xpcasTestProcedureBoolean2 FOR "aero.minova.cas.controller.SQLProcedureControllerTest.testBoolean2";
CREATE ALIAS xpcasTestProcedureInstant FOR "aero.minova.cas.controller.SQLProcedureControllerTest.testInstant";
CREATE ALIAS xpcasTestProcedureInstant2 FOR "aero.minova.cas.controller.SQLProcedureControllerTest.testInstant2";