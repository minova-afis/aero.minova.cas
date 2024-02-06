alter view dbo.xvcasUserIndex
with encryption as
	select	u.KeyLong,
			u.KeyText,
			u.UserSecurityToken,
			u.Memberships,
			u.LastAction
	from xtcasUser u
	where u.LastAction > 0