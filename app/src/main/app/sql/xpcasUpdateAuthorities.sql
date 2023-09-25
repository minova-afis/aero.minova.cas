alter procedure dbo.xpcasUpdateAuthorities (
	@KeyLong int,
	@Username nvarchar(50),
	@Authority nvarchar(50),
	@KeyText nvarchar(50) = null
)
with encryption as
	if (exists(select * from xtcasAuthorities
		where Authority = @Authority
		  and Username = @Username
		  and KeyLong <> @KeyLong
		  and LastAction > 0))
	begin
		raiserror('ADO | 25 | msg.sql.DuplicateMatchcodeNotAllowed', 16, 1) with seterror
		return -1
	end

	update xtcasAuthorities
	set	Authority = @Authority,
		KeyText = @KeyText,
		LastUser = dbo.xfCasUser(),
		LastDate = getDate(),
		LastAction = 2
	where KeyLong = @KeyLong
	return @@error