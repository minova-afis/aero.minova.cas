alter procedure dbo.xpcasInsertLuUserPrivilegeUserGroup (
	@KeyLong int,
	@UserPrivilegeKey int = null,
	@UserGroupKey int = null,
	@RowLevelSecurity bit = 0
)
with encryption as
	declare @returnCode int
	execute @returnCode = xpcasUpdateLuUserPrivilegeUserGroup
		@KeyLong output,
		@UserPrivilegeKey,
		@UserGroupKey,
		@RowLevelSecurity
return @returnCode