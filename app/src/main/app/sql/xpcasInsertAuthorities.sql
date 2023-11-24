alter procedure dbo.xpcasInsertAuthorities (
	@KeyLong int output,
	@Username nvarchar(50),
	@Authority int,
	@KeyText nvarchar(50) = null
)
with encryption as
	-- Der übergebene Authority Key ist eigentlich der UserGroupKey
	if exists (select * from xtcasUserGroup where KeyLong = @Authority)
	begin
	
		declare @UserGroup nvarchar(50)
		select @UserGroup=KeyText from xtcasUserGroup where KeyLong = @Authority


		if exists(select * from xtcasAuthorities
			where Username = @Username
			and Authority = @UserGroup
			and LastAction > 0)
		begin
			raiserror('ADO | 25 | msg.sql.DuplicateMatchcodeNotAllowed', 16, 1) with seterror
			return -1
		end
		
		insert into xtcasAuthorities (
			Username,
			Authority,
			KeyText,
			LastAction,
			LastUser,
			LastDate
		) values (
			@Username,
			@UserGroup,
			@KeyText,
			1,
			dbo.xfCasUser(),
			getDate()
		)

		select @KeyLong = @@identity
	end
	return @@error