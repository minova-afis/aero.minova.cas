alter procedure dbo.xpcasDeleteUserPrivilege (
	@KeyLong int
)
with encryption as
	update xtcasUserPrivilege
	set	LastAction = -1,
		LastUser = system_user,
		LastDate = getdate()
	where KeyLong = @KeyLong
return @@error