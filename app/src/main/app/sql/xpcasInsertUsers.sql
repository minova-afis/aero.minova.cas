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

	if (len(@Password)<60 or LEFT(@Password, 2)<>'$2')
	begin
		raiserror('ADO | 25 | msg.sql.PasswordNotEncrypted!', 16, 1) with seterror
		return -1
	end

	insert into xtcasUsers (
		Username,
		Password,
		LastAction,
		LastDate,
		LastUser
	) values (
		@Username,
		@Password,
		1,
		getDate(),
		dbo.xfCasUser()
	)

	select @KeyLong = @@identity
	return @@error