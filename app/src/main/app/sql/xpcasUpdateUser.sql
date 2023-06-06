alter procedure dbo.xpcasUpdateUser (
	@KeyLong int,
	@KeyText nvarchar(50) = null,
	@UserSecurityToken nvarchar(50) = null,
	@Memberships nvarchar(250) = null
)
with encryption as
	if (exists(select * from xtcasUser
		where KeyText = @KeyText
		  and KeyLong <> @KeyLong
		  and LastAction > 0))
	begin
		raiserror('ADO | 25 | msg.sql.DuplicateMatchcodeNotAllowed', 16, 1) with seterror
		return -1
	end

	update xtcasUser
	set KeyText = @KeyText,
		UserSecurityToken = @UserSecurityToken,
		Memberships = @Memberships,
		LastAction = 2,
		LastDate = getDate(),
		LastUser = dbo.xfCasUser()
	where KeyLong = @KeyLong
return @@error