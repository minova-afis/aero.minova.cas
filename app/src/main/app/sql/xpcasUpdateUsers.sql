alter procedure dbo.xpcasUpdateUsers (
	@KeyLong int output,
	@Username nvarchar(50),
	@Password nvarchar(100) = null
)
with encryption as
	if exists(select * from xtcasUsers
		where Username = @Username
		  and KeyLong <> @KeyLong
		  and LastAction > 0)
	begin
		raiserror('ADO | 25 | msg.sql.DuplicateMatchcodeNotAllowed', 16, 1) with seterror
		return -1
	end

	declare @OldPassword nvarchar(100)

	if (len(@Password)<60 or LEFT(@Password, 2)<>'$2')
	begin
		raiserror('ADO | 25 | msg.sql.PasswordNotEncrypted!', 16, 1) with seterror
		return -1
	end

	select @OldPassword = Password 
	from xtcasUsers 
	where KeyLong = @KeyLong

	-- Nur das Password wird geändert
	if exists( select * from xtcasUsers where Username = @Username and LastAction > 0)
	begin 
		update xtcasUsers
		set	Password = coalesce(@Password, @OldPassword),
			LastAction = 2,
			LastDate = getDate(),
			LastUser = dbo.xfCasUser()
		where KeyLong = @KeyLong

	end
	return @@error