CREATE OR REPLACE VIEW public.xvcasUserIndex
as
	select	u.KeyLong,
			u.KeyText,
			u.UserSecurityToken,
			u.Memberships,
			u.LastAction
	from xtcasUser u
	where u.LastAction > 0;