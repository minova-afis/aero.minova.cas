alter procedure dbo.xpcasInsertUser (
	@KeyLong int output,
	@KeyText nvarchar(10) = null,
	@UserSecurityToken nvarchar(50) = null,
	@Memberships nvarchar(250) = null
)
with encryption as
	if (exists(select * from xtcasUser
		where KeyText = @KeyText
		  and LastAction > 0))
	begin
		raiserror('ADO | 25 | msg.sql.DuplicateMatchcodeNotAllowed', 16, 1) with seterror
		return -1
	end

	insert into xtcasUser (
		KeyText,
		UserSecurityToken,
		Memberships
	) values (
		@KeyText,
		@UserSecurityToken,
		@Memberships
	)

	select @KeyLong = @@identity
return @@error