
DROP VIEW IF EXISTS xvcasUserSecurity

create view xvcasUserSecurity as--
	select	up.KeyLong,--
			up.KeyText as PrivilegeKeyText,--
			ug.SecurityToken,--
			upg.RowLevelSecurity,--
			ug.LastDate--
	from xtcasUserPrivilege up--
	inner join xtcasLuUserPrivilegeUserGroup upg on up.KeyLong = upg.UserPrivilegeKey--
	inner join xtcasUserGroup ug on ug.KeyLong = upg.UserGroupKey--
