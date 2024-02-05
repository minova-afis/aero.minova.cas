CREATE OR REPLACE VIEW public.xvcasUserIndex2
 as
 select u.KeyLong,
			u.KeyText,
			u.UserSecurityToken as Description,
			u.Memberships,
			u.LastAction,
			u.LastDate,
			u.LastUser,
			u.LastAction
	from xtcasUser u
	where u.LastAction > 0;