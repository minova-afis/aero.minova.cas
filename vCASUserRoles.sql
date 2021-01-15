CREATE VIEW vCASUserRoles 
WITH ENCRYPTION
AS SELECT U.KeyText, U.UserSecurityToken, UG.KeyLong
FROM tUserRoles UR
JOIN tUser U ON UR.UserKey = U.KeyLong
JOIN tUserGroup UG ON UG.KeyLong = UR.UserGroupKey 