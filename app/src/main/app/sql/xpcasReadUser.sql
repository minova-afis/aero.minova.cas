alter procedure dbo.xpcasReadUser (
	@KeyLong int output,
	@KeyText nvarchar(50) = null output,
	@UserSecurityToken nvarchar(50) = null output,
	@Memberships nvarchar(250) = null output
)
with encryption as
	if (@KeyLong is null)
	begin
		select @KeyLong = KeyLong from xtcasUser where KeyText = @KeyText and LastAction > 0
	end

	select	@KeyText = KeyText,
			@UserSecurityToken = UserSecurityToken,
			@Memberships = Memberships
	from xtcasUser
	where KeyLong = @KeyLong
return @@error