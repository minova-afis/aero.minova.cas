alter view dbo.xvcasUserPrivilegeIndex
with encryption as
	select	up.KeyLong,
			up.KeyText,
			up.Description
	from xtcasUserPrivilege up
	where up.LastAction > 0