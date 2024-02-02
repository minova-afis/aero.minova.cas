CREATE OR REPLACE VIEW public.xvcasUserGroupIndex
as
	select	distinct 
			ug.KeyLong,
			ug.KeyText,
			ug.Description,
			ug.UserCode,
			ug.SecurityToken,
			ug.KeyLong as UserPrivilegeKey
	from xtcasUserGroup ug
	where ug.LastAction > 0;