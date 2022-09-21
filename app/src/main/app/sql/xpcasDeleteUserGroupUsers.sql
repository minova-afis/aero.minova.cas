alter procedure dbo.xpcasDeleteUserGroupUsers (
	@KeyLong int,
	@UsersKey int = null output
)
with encryption as

	declare @UserGroupText nvarchar(50)
	select @UserGroupText=KeyText from xtcasUserGroup where KeyLong=@KeyLong


		declare @Username nvarchar(50)
		select @Username=Username from xtcasUsers where KeyLong = @UsersKey

		update xtcasAuthorities
		set	LastAction = -1,
			LastUser = dbo.xfCasUser(),
			LastDate = getdate()
		where Username = @Username
		and Authority = @UserGroupText

return @@error