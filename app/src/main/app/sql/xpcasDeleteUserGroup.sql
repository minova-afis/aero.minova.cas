alter procedure dbo.xpcasDeleteUserGroup (
	@KeyLong int
)
with encryption as
	update xtcasUserGroup
	set	LastAction = -1,
		LastUser = system_user,
		LastDate = getdate()
	where KeyLong = @KeyLong
return @@error