alter procedure dbo.xpcasInsertUsers (
	@KeyLong int output,
	@Username nvarchar(50),
	@Password nvarchar(100)
)
with encryption as
	if (exists(select * from xtcasUsers
		where Username = @Username
		  and LastAction > 0))
	begin
		raiserror('ADO | 25 | msg.sql.DuplicateMatchcodeNotAllowed', 16, 1) with seterror
		return -1
	end

	insert into xtcasUsers (
		Username,
		Password
	) values (
		@Username,
		@Password
	)

	select @KeyLong = @@identity
return @@error