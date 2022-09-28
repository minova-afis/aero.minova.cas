alter procedure dbo.xpcasUpdateAuthorities (
	@KeyLong int,
	@Username nvarchar(50),
	@Authority nvarchar(50)
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
	set	Authority = @Authority
	where KeyLong = @KeyLong
	return @@error