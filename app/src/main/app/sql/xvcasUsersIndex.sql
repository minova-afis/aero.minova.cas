alter view dbo.xvcasUsersIndex
with encryption as
	select	u.KeyLong,
			u.KeyText,
			u.Username,
			u.Description,
			'XXXXX' as Password
	from xtcasUsers u
	where u.LastAction > 0