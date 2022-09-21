alter procedure dbo.xpcasInsertUserGroupUsers (
	@KeyLong int output,
	@UsersKey int
) with encryption as

	declare @UserName NVARCHAR(50)
	select @UserName = Username from xtcasUsers where KeyLong=@UsersKey

    exec xpcasInsertAuthorities null, @UserName, @KeyLong

select @KeyLong = @@identity
return @@error