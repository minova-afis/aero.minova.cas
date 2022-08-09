alter view dbo.xvcasUserGroupIndex
with encryption as
	select	distinct 
			ug.KeyLong,
			ug.KeyText,
			ug.Description,
			ug.UserCode,
			ug.SecurityToken,
			ug.KeyLong as UserPrivilegeKey,
			lu.RowLevelSecurity
	from xtcasUserGroup ug
	left join xtcasLuUserPrivilegeUserGroup lu on lu.UserGroupKey = ug.KeyLong
	left join xtcasUserPrivilege up on  up.KeyLong=lu.UserPrivilegeKey
	where ug.LastAction > 0