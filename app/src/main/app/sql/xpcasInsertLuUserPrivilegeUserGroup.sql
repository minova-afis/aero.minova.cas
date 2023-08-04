alter procedure dbo.xpcasInsertLuUserPrivilegeUserGroup (
	@KeyLong int output,
	@UserPrivilegeKey int = null,
	@UserGroupKey int = null,
	@RowLevelSecurity bit = 0,
	@KeyText nvarchar(50) = null
)
with encryption as
	declare @returnCode int
	execute @returnCode = xpcasUpdateLuUserPrivilegeUserGroup 
		@KeyLong,
		@UserPrivilegeKey,
		@UserGroupKey,
		@RowLevelSecurity,
		@KeyText
return @returnCode