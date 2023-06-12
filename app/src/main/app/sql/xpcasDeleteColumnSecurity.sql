alter procedure dbo.xpcasDeleteColumnSecurity (
	@KeyLong int
)
with encryption as
	update xtcasColumnSecurity
	set	LastAction = -1,
		LastUser = dbo.xfCasUser(),
		LastDate = getdate()
	where KeyLong = @KeyLong
return @@error