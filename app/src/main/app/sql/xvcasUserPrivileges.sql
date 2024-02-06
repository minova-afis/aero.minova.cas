alter view xvcasUserPrivileges
with encryption as
	select	up.KeyLong,
			up.KeyText as PrivilegeKeyText,
			ug.KeyText as KeyText,
			upg.RowLevelSecurity,
			up.LastAction
	from xtcasUserPrivilege up
	inner join xtcasLuUserPrivilegeUserGroup upg on up.KeyLong = upg.UserPrivilegeKey and upg.LastAction > 0
	inner join xtcasUserGroup ug on ug.KeyLong = upg.UserGroupKey and ug.LastAction > 0
	where up.LastAction > 0