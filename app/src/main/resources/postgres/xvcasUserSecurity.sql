CREATE OR REPLACE VIEW public.xvcasUserSecurity
AS
SELECT up.KeyLong,
			up.KeyText AS PrivilegeKeyText,
			ug.SecurityToken,
			upg.RowLevelSecurity
	FROM xtcasUserPrivilege up
	INNER JOIN xtcasLuUserPrivilegeUserGroup upg ON up.KeyLong = upg.UserPrivilegeKey AND upg.LastAction > 0
	INNER JOIN xtcasUserGroup ug ON ug.KeyLong = upg.UserGroupKey AND ug.LastAction > 0
	WHERE up.LastAction > 0;