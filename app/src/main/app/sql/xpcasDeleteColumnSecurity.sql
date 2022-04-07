alter procedure dbo.xpcasDeleteColumnSecurity (
	@KeyLong int
)
with encryption as
	update xtcasColumnSecurity
	set	LastAction = -1,
		LastUser = system_user,
		LastDate = getdate()
	where KeyLong = @KeyLong
return @@error