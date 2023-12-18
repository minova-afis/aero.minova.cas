CREATE OR REPLACE VIEW public.xvcasUsersIndex2
 as
	select	u.KeyLong,
			u.Username as KeyText,
			u.Username as Description,
			u.LastAction,
			u.LastDate,
			u.LastUser
	from xtcasUsers u
	where u.LastAction > 0;