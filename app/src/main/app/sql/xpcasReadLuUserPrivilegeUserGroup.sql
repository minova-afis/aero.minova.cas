alter procedure dbo.xpcasReadLuUserPrivilegeUserGroup (
	@KeyLong int,
	@UserPrivilegeKey int = null,
	@UserGroupKey int = null,
	@RowLevelSecurity bit = 0
)
with encryption as
	if (@UserPrivilegeKey is not null)
	begin
		select	KeyLong,
				UserPrivilegeKey,
				UserGroupKey,
				RowLevelSecurity
		from xtcasLuUserPrivilegeUserGroup
		where UserPrivilegeKey = @UserPrivilegeKey
		  and LastAction > 0
	end
	else begin
		select	KeyLong,
				UserPrivilegeKey,
				UserGroupKey,
				RowLevelSecurity
		from xtcasLuUserPrivilegeUserGroup
		where UserGroupKey = @UserGroupKey
		  and LastAction > 0
	end
return @@error