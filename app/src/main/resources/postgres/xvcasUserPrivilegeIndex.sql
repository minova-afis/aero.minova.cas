CREATE OR REPLACE VIEW public.xvcasUserPrivilegeIndex
as
	select	distinct
			up.KeyLong,
			up.KeyText,
			up.Description,
			ug.KeyLong as UserGroupKey,
			lu.RowLevelSecurity
	from xtcasUserPrivilege up
	left outer join xtcasLuUserPrivilegeUserGroup lu on lu.UserPrivilegeKey = up.KeyLong
	left outer join xtcasUserGroup ug on ug.KeyLong=lu.UserGroupKey
	where up.LastAction > 0 
		and lu.LastAction > 0
		and ug.LastAction > 0;