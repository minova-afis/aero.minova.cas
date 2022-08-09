alter procedure dbo.xpcasDeleteUsers (
	@KeyLong int
)
with encryption as

	declare @Username nvarchar(50)

	select @Username=Username from xtcasUsers where KeyLong = @KeyLong

	-- Davor müssen noch alle Authrities zu dem Usernamen gelöscht werden.
	update xtcasAuthorities
	set LastAction = -1,
		LastUser = dbo.xfCasUser(),
		LastDate = getdate()
	where Username = @Username

	update xtcasUsers
	set	LastAction = -1,
		LastUser = dbo.xfCasUser(),
		LastDate = getdate()
	where KeyLong = @KeyLong

return @@error