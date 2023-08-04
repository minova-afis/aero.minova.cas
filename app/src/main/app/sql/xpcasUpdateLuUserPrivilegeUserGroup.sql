alter procedure dbo.xpcasUpdateLuUserPrivilegeUserGroup (
	@KeyLong int output,
	@UserPrivilegeKey int = null,
	@UserGroupKey int = null,
	@RowLevelSecurity bit = 0,
	@KeyText NVARCHAR(50) = null
)
with encryption as
	if (@UserPrivilegeKey is null or @UserGroupKey is null)
	begin
		-- kein Speichern möglich
		return -1
	end

	if (not exists(select 1 from xtcasLuUserPrivilegeUserGroup
		where UserPrivilegeKey = @UserPrivilegeKey
		  and UserGroupKey = @UserGroupKey))
	begin
		insert into xtcasLuUserPrivilegeUserGroup (UserPrivilegeKey, UserGroupKey, RowLevelSecurity, KeyText, LastAction, LastDate, LastUser)
		values (@UserPrivilegeKey, @UserGroupKey, @RowLevelSecurity, @KeyText, 1, getDate(), dbo.xfCasUser())
	end
	else if (exists(select 1 from xtcasLuUserPrivilegeUserGroup
		where UserPrivilegeKey = @UserPrivilegeKey
		  and UserGroupKey = @UserGroupKey
		  and LastAction < 0))
	begin
		update xtcasLuUserPrivilegeUserGroup
		set RowLevelSecurity = @RowLevelSecurity,
			KeyText = @KeyText,
			LastAction = 2,
			LastDate = getDate(),
			LastUser = dbo.xfCasUser()
		where UserPrivilegeKey = @UserPrivilegeKey
		  and UserGroupKey = @UserGroupKey
	end
	else begin
		update xtcasLuUserPrivilegeUserGroup
		set UserPrivilegeKey = @UserPrivilegeKey,
			UserGroupKey = @UserGroupKey,
			RowLevelSecurity = @RowLevelSecurity,
			KeyText = @KeyText,
			LastAction = 2,
			LastDate = getDate(),
			LastUser = dbo.xfCasUser()
		where KeyLong = @KeyLong
	end
return @@error