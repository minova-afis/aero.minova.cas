alter view dbo.xvcasUserGroupIndex
with encryption as
	select	ug.KeyLong,
			ug.KeyText,
			ug.Description,
			ug.UserCode,
			ug.SecurityToken
	from xtcasUserGroup ug
	where ug.LastAction > 0