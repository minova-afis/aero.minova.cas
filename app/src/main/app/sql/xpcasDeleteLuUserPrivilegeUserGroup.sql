alter procedure dbo.xpcasDeleteLuUserPrivilegeUserGroup (
	@KeyLong int,
	@UserPrivilegeKey int = null,
	@UserGroupKey int = null,
	@RowLevelSecurity bit = 0
)
with encryption as
	update xtcasLuUserPrivilegeUserGroup
	set	LastAction = -1,
		LastUser = dbo.xfCasUser(),
		LastDate = getdate()
	where KeyLong = @KeyLong
return @@error