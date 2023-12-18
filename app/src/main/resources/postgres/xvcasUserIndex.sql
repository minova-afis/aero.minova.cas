CREATE OR REPLACE VIEW public.xvcasUserIndex
as
	select	u.KeyLong,
			u.KeyText,
			u.UserSecurityToken,
			u.Memberships
	from xtcasUser u
	where u.LastAction > 0;