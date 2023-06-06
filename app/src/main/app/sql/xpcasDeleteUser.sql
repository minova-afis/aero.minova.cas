alter procedure dbo.xpcasDeleteUser (
	@KeyLong int
)
with encryption as
	update xtcasUser
	set	LastAction = -1,
		LastUser = dbo.xfCasUser(),
		LastDate = getdate()
	where KeyLong = @KeyLong
return @@error