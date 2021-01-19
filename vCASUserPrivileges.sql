CREATE VIEW vCASUserPrivileges
with encryption AS
select UP.KeyText  as PrivilegeKeyText, UG.KeyText as KeyText
from 
tUserPrivilege UP
join tLuUserPrivilegeUserGroup UPG on UP.KeyLong=UPG.UserPrivilegeKey
join tUserGroup UG on UG.KeyLong=UPG.UserGroupKey