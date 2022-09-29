alter procedure dbo.xpcasInsertAuthorities (
	@KeyLong int output,
	@Username nvarchar(50),
	@Authority int
)
with encryption as
	if exists(select * from xtcasAuthorities
		where Username = @Username
		  and LastAction > 0)
	begin
		raiserror('ADO | 25 | msg.sql.DuplicateMatchcodeNotAllowed', 16, 1) with seterror
		return -1
	end

	-- Der übergebene Authority Key ist eigentlich der UserGroupKey
	if exists (select * from xtcasUserGroup where KeyLong = @Authority)
	begin

	declare @UserGroup nvarchar(50)
	select @UserGroup=KeyText from xtcasUserGroup where KeyLong = @Authority

	insert into xtcasAuthorities (
		Username,
		Authority
	) values (
		@Username,
		@UserGroup
	)

	select @KeyLong = @@identity
	end
	return @@error