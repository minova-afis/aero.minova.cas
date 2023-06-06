alter procedure dbo.xpcasDeleteServiceProperties (
	@KeyLong int
)
with encryption as
	update xtcasServiceProperties
	set	LastAction = -1,
		LastUser = dbo.xfCasUser(),
		LastDate = getdate()
	where KeyLong = @KeyLong

    return @@error