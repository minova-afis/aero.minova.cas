CREATE VIEW xvcasUserPrivileges
with encryption AS
select UP.KeyLong, UP.KeyText as PrivilegeKeyText, UG.KeyText as KeyText, UPG.RowLevelSecurity
from 
tUserPrivilege UP
join xtcasLuUserPrivilegeUserGroup UPG on UP.KeyLong=UPG.UserPrivilegeKey
join xtcasUserGroup UG on UG.KeyLong=UPG.UserGroupKey
