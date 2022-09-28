alter procedure dbo.xpcasReadLuUserPrivilegeUserGroup (
	@KeyLong int,
	@UserPrivilegeKey int,
	@UserGroupKey int
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