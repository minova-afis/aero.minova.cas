alter procedure dbo.xpcasDeleteUsers (
	@KeyLong int
)
with encryption as

	declare @Username nvarchar(50)

	select @Username=Username from xtcasUsers where KeyLong = @KeyLong

	if exists (select * from xtcasAuthorities where Username = @Username )
	begin
		raiserror ('ADO | 25 | msg.UsersAuthoritiesDeleteUser | Cannot delete user because there is at least one mapped user group for this user. Please delete all mappings for this user first.', 16,1 ) with seterror
	return 1
	end 

	update xtcasUsers
	set	LastAction = -1,
		LastUser = dbo.xfCasUser(),
		LastDate = getdate()
	where KeyLong = @KeyLong

	return @@error