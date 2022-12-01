alter procedure dbo.xpcasDeleteUserGroupUsers (
	@KeyLong int,
	@UsersKey int output
)
with encryption as

	declare @UserGroupText nvarchar(50)
	select @UserGroupText=KeyText from xtcasUserGroup where KeyLong=@KeyLong


	declare @Username nvarchar(50)
	select @Username=Username from xtcasUsers where KeyLong = @UsersKey

	delete from xtcasAuthorities
	where Username = @Username
	and Authority = @UserGroupText

	return @@error