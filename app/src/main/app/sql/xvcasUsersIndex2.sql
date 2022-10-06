alter view dbo.xvcasUsersIndex2
with encryption as
	select	u.KeyLong,
			u.Username as KeyText,
			u.Username as Description,
			u.LastAction,
			u.LastDate,
			u.LastUser
	from xtcasUsers u
	where u.LastAction > 0