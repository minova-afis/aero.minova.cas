alter procedure dbo.xpcasUpdateUserGroup (
	@KeyLong int,
	@KeyText nvarchar(50) = null,
	@Description nvarchar(50) = null,
	@UserCode nvarchar(50) = null,
	@SecurityToken nvarchar(250) = null
)
with encryption as

	if (exists(select * from xtcasUserGroup
		where KeyText = @KeyText
		  and KeyLong <> @KeyLong
		  and LastAction > 0))
	begin
		raiserror('ADO | 25 | msg.sql.DuplicateMatchcodeNotAllowed', 16, 1) with seterror
		return -1
	end

	declare @UserCodeOld nvarchar(50)

	select @UserCodeOld = UserCode from xtcasUserGroup where KeyLong=@KeyLong


	update xtcasUserGroup
	set KeyText = @KeyText,
		Description = @Description,
		UserCode = @UserCode,
		SecurityToken = @SecurityToken
	where KeyLong = @KeyLong
	

	if (@UserCodeOld<>@UserCode)
	begin

		declare @Memberships nvarchar(max),
		@UserKey int
		

		declare user_cursor cursor for 
		select KeyLong, Memberships 
		from xtcasUser

		open user_cursor
		fetch next from user_cursor into @UserKey, @Memberships 
	
		while @@FETCH_STATUS = 0 
		begin

			declare @UserGroupIsInMembership int

			-- Überprüfen, ob es innerhalb der Memberships eine Membership der UserGroup gibt.
			select @Memberships=Memberships from xtcasUser where KeyLong=@UserKey and LastAction > 0
			select @UserGroupIsInMembership = CHARINDEX(@UserCodeOld, @Memberships) 

			if (@UserGroupIsInMembership <> 0)
			begin 
				exec xpcasDeleteUserGroupUser @KeyLong,@UserKey
				exec xpcasInsertUserGroupUser @KeyLong,@UserKey
			end
		
			fetch next from user_cursor into @UserKey, @Memberships 
		end

		close user_cursor 
		deallocate user_cursor 
	end


return @@error