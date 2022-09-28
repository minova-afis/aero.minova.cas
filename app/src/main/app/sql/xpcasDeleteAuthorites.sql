alter procedure dbo.xpcasDeleteAuthorities (
	@KeyLong int,
	@Username nvarchar(50) = null,
	@Authority nvarchar(50) = null
)
with encryption as
	update xtcasAuthorities
	set	LastAction = -1,
		LastUser = dbo.xfCasUser(),
		LastDate = getdate()
	where KeyLong = @KeyLong
	return @@error