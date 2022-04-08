alter view dbo.xvcasUserIndex
with encryption as
	select	u.KeyLong,
			u.KeyText,
			u.UserSecurityToken,
			u.Memberships
	from xtcasUser u
	where u.LastAction > 0