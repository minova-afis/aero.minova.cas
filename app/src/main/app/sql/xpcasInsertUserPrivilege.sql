alter procedure dbo.xpcasInsertUserPrivilege (
	@KeyLong int output,
	@KeyText nvarchar(200) = null,
	@Description nvarchar(50) = null
)
with encryption as
	if (exists(select * from xtcasUserPrivilege
		where KeyText = @KeyText
		  and LastAction > 0))
	begin
		raiserror('ADO | 25 | msg.sql.DuplicateMatchcodeNotAllowed', 16, 1) with seterror
		return -1
	end

	insert into xtcasUserPrivilege (
		KeyText,
		Description,
		LastAction,
		LastDate,
		LastUser
	) values (
		@KeyText,
		@Description,
		1,
		getDate(),
		dbo.xfCasUser()
	)

	select @KeyLong = @@identity
return @@error