alter view dbo.xvcasUserGroupIndex
with encryption as
	select	distinct 
			ug.KeyLong,
			ug.KeyText,
			ug.Description,
			ug.UserCode,
			ug.SecurityToken,
			ug.KeyLong as UserPrivilegeKey,
			ug.LastAction
	from xtcasUserGroup ug
	where ug.LastAction > 0