alter view dbo.xvcasUserIndex2
with encryption as
	select	u.KeyLong,
			u.KeyText,
			u.UserSecurityToken as Description,
			u.Memberships,
			u.LastAction,
			u.LastDate,
			u.LastUser
	from xtcasUser u
	where u.LastAction > 0