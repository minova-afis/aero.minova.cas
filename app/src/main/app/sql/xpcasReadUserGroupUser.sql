alter procedure dbo.xpcasReadUserGroupUser (
	@KeyLong int,
	@UserKey int
)
with encryption as

    declare @UserGroupToken nvarchar(50)
	declare @Memberships nvarchar(250)

	-- Den KeyText der UserGroup herausbekommen.
	select @UserGroupToken=KeyText from xtcasUserGroup where KeyLong=@KeyLong

	create table #temp(
		KeyLong  int,
		UserKey int
	);

	declare user_cursor cursor for 
    select KeyLong, Memberships 
    from dbo.xtcasUser 

	open user_cursor
	fetch next from user_cursor into @UserKey, @Memberships 
 
	while @@FETCH_STATUS = 0 
	begin

		declare @UserGroupIsInMembership int

    	-- Überprüfen, ob es innerhalb der Memberships eine Membership der UserGroup gibt.
    	select @Memberships=Memberships from xtcasUser where KeyLong=@UserKey and LastAction > 0
    	select @UserGroupIsInMembership = CHARINDEX(@UserGroupToken, @Memberships) 

    	if (@UserGroupIsInMembership <> 0)
		begin
			insert into #temp (KeyLong, UserKey) values (@KeyLong, @UserKey)
		end
	
		fetch next from user_cursor into @UserKey, @Memberships 
	end

	close user_cursor 
	deallocate user_cursor 

	select * from #temp

	drop table #temp

	return @@error