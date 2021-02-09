CREATE VIEW xvcasUserPrivileges
with encryption AS
select UP.KeyLong, UP.KeyText as PrivilegeKeyText, UG.KeyText as KeyText, UG.RowLevelSecurity
from 
tUserPrivilege UP
join tLuUserPrivilegeUserGroup UPG on UP.KeyLong=UPG.UserPrivilegeKey
join xtcasUserGroup UG on UG.KeyLong=UPG.UserGroupKey
