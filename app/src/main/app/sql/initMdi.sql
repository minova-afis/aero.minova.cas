-- General Application Info
exec xpcasInitMdi null, 'CAS', '@CAS', null, null, null, 3, 'aero.minova.cas'

-- Config Menu
exec xpcasInitMdi 'config', null, '@cas.config', null, 10.0, 'admin', 2, 'aero.minova.cas'
exec xpcasInitMdi 'ServiceProperties', 'xtcasServiceProperties', '@xtcasServiceProperties', 'config', 10.0, 'admin', 1, 'aero.minova.cas'

-- CAS Menu
exec xpcasInitMdi 'CAS', null, '@CAS', null, 20.0, 'admin', 2, 'aero.minova.cas'
exec xpcasInitMdi 'ColumnSecurity', 'ColumnSecurity', '@xtcasColumnSecurity', 'CAS', 11.0, 'admin', 1, 'aero.minova.cas'
exec xpcasInitMdi 'UserGroup', 'UserGroup', '@xtcasUserGroup', 'CAS', 12.0, 'admin', 1, 'aero.minova.cas'
exec xpcasInitMdi 'UserPrivilege', 'UserPrivilege', '@xtcasUserPrivilege', 'CAS', 13.0, 'admin', 1, 'aero.minova.cas'
exec xpcasInitMdi 'Mdi', 'Menu', '@xtcasMdi', 'CAS', 14.0, 'admin', 1, 'aero.minova.cas'
exec xpcasInitMdi 'User', 'LDAPUser', '@xtcasUser', 'CAS', 10.0, 'admin', 1, 'aero.minova.cas'
exec xpcasInitMdi 'Users', 'DBUser', '@xtcasUsers', 'CAS', 10.0, 'admin', 1, 'aero.minova.cas'
