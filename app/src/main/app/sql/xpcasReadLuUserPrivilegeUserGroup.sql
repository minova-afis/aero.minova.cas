alter procedure dbo.xpcasReadLuUserPrivilegeUserGroup (
	@KeyLong int output,
	@UserPrivilegeKey int output,
	@UserGroupKey int output,
	@RowLevelSecurity bit output,
	@KeyText NVARCHAR(50) =null output
)
with encryption as
	if (@UserPrivilegeKey is not null)
	begin
		select	@KeyLong = KeyLong,
				@UserGroupKey = UserGroupKey,
				@RowLevelSecurity = RowLevelSecurity,
				@KeyText = KeyText
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