alter view dbo.xvcasUsersIndex
with encryption as
	select	u.KeyLong,
			u.Username,
			u.Description,
			u.Password
	from xtcasUsers u
	where u.LastAction > 0