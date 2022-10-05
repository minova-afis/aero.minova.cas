alter procedure dbo.xpcasUpdateUserGroup (
	@KeyLong int,
	@KeyText nvarchar(50) = null,
	@Description nvarchar(50) = null,
	@UserCode nvarchar(50) = null,
	@SecurityToken nvarchar(250) = null
)
with encryption as

	if (exists(select * from xtcasUserGroup
		where KeyText = @KeyText
		  and KeyLong <> @KeyLong
		  and LastAction > 0))
	begin
		raiserror('ADO | 25 | msg.sql.DuplicateMatchcodeNotAllowed', 16, 1) with seterror
		return -1
	end

	declare @KeytextOld nvarchar(50)

	select @KeytextOld = Keytext from xtcasUserGroup where KeyLong=@KeyLong


	update xtcasUserGroup
	set KeyText = @KeyText,
		Description = @Description,
		UserCode = @UserCode,
		SecurityToken = @SecurityToken
	where KeyLong = @KeyLong
	

	if (@KeytextOld<>@KeyText)
	begin
		raiserror('ADO | 25 | msg.sql.KeytextNoChangeAllowed | Der KeyText darf nicht verändert werden.', 16, 1) with seterror
		return -1
	end


return @@error