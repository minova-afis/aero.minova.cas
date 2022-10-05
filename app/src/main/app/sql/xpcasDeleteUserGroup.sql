alter procedure dbo.xpcasDeleteUserGroup (
	@KeyLong int
)
with encryption as
	update xtcasUserGroup
	set	LastAction = -1,
		LastUser = system_user,
		LastDate = getdate()
	where KeyLong = @KeyLong

	declare @KeytextOld nvarchar(50)

	select @KeytextOld = Keytext from xtcasUserGroup where KeyLong=@KeyLong

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
		select @UserGroupIsInMembership = CHARINDEX(@KeytextOld, @Memberships) 

		if (@UserGroupIsInMembership <> 0)
		begin 
			exec xpcasDeleteUserGroupUser @KeyLong,@UserKey
		end
	
		fetch next from user_cursor into @UserKey, @Memberships 
	end

	close user_cursor 
	deallocate user_cursor 



return @@error