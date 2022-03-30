alter procedure dbo.xpcasUpdateLuUserPrivilegeUserGroup (
	@KeyLong int,
	@UserPrivilegeKey int = null,
	@UserGroupKey int = null,
	@RowLevelSecurity bit = 0
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
		insert into xtcasLuUserPrivilegeUserGroup (UserPrivilegeKey, UserGroupKey, RowLevelSecurity)
		values (@UserPrivilegeKey, @UserGroupKey, @RowLevelSecurity)
	end
	else if (exists(select 1 from xtcasLuUserPrivilegeUserGroup
		where UserPrivilegeKey = @UserPrivilegeKey
		  and UserGroupKey = @UserGroupKey
		  and LastAction < 0))
	begin
		update xtcasLuUserPrivilegeUserGroup
		set RowLevelSecurity = @RowLevelSecurity
		where UserPrivilegeKey = @UserPrivilegeKey
		  and UserGroupKey = @UserGroupKey
	end
	else begin
		update xtcasLuUserPrivilegeUserGroup
		set UserPrivilegeKey = @UserPrivilegeKey,
			UserGroupKey = @UserGroupKey,
			RowLevelSecurity = @RowLevelSecurity
		where KeyLong = @KeyLong
	end
return @@error