alter view dbo.xvcasUsersIndex
with encryption as
	select	u.KeyLong,
			u.KeyText,
			u.Username,
			u.Description,
			'XXXXX' as Password,
			u.LastAction
	from xtcasUsers u
	where u.LastAction > 0