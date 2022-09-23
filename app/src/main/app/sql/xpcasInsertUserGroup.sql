alter procedure dbo.xpcasInsertUserGroup (
	@KeyLong int output,
	@KeyText nvarchar(50) = null,
	@Description nvarchar(50) = null,
	@UserCode nvarchar(50) = null,
	@SecurityToken nvarchar(250) = null
)
with encryption as
	if (exists(select * from xtcasUserGroup
		where KeyText = @KeyText
		  and LastAction > 0))
	begin
		raiserror('ADO | 25 | msg.sql.DuplicateMatchcodeNotAllowed', 16, 1) with seterror
		return -1
	end

	insert into xtcasUserGroup (
		KeyText,
		Description,
		UserCode,
		SecurityToken
	) values (
		@KeyText,
		@Description,
		@UserCode,
		@SecurityToken
	)

	select @KeyLong = @@identity
	return @@error