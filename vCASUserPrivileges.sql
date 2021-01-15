CREATE VIEW vCASUserPrivileges
with encryption AS
select UP.KeyText  as PrivilegeKeyText, UG.KeyLong as KeyLong
from 
tUserPrivilege UP
join tLuUserPrivilegeUserGroup UPG on UP.KeyLong=UPG.UserPrivilegeKey
join tUserGroup UG on UG.KeyLong=UPG.UserGroupKey