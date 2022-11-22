-- General Application Info
exec xpcasInitMdi null, 'CAS.ico', 'CAS', null, null, null, 3

-- Config Menu
exec xpcasInitMdi 'config', null, '@cas.config', null, 10.0, '#admin', 2
exec xpcasInitMdi 'ServiceProperties', 'xtcasServiceProperties.ico', '@xtcasServiceProperties', 'config', 10.0, '#admin', 1

-- CAS Menu
exec xpcasInitMdi 'CAS', null, 'CAS', null, 20.0, '#admin', 2
exec xpcasInitMdi 'ColumnSecurity', 'ColumnSecurity', '@xtcasColumnSecurity', 'CAS', 11.0, '#admin', 1
exec xpcasInitMdi 'UserGroup', 'UserGroup', '@xtcasUserGroup', 'CAS', 12.0, '#admin', 1
exec xpcasInitMdi 'UserPrivileges', 'UserPrivilege', '@xtcasUserPrivilege', 'CAS', 13.0, '#admin', 1
exec xpcasInitMdi 'Mdi', 'Menu', '@xtcasMdi', 'CAS', 14.0, '#admin', 1
exec xpcasInitMdi 'User', 'LDAPUser', '@xtcasUser', 'Stundenerfassung', 10.0, '#admin', 1
exec xpcasInitMdi 'Users', 'DBUser', '@xtcasUsers', 'DatenbankLogin', 10.0, '#admin', 1
