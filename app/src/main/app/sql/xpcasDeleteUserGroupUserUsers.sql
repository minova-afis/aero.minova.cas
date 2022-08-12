alter procedure dbo.xpcasDeleteUserGroupUserUsers (
	@KeyLong int,
	@UsersKey int = null output,
	@UserKey int = null output
)
with encryption as

	declare @UserGroupText nvarchar(50)
	select @UserGroupText=KeyText from xtcasUserGroup where KeyLong=@KeyLong

	if (@UsersKey is not null)
	begin 
		declare @Username nvarchar(50)
		select @Username=Username from xtcasUsers where KeyLong = @UsersKey

		update xtcasAuthorities
		set	LastAction = -1,
			LastUser = dbo.xfCasUser(),
			LastDate = getdate()
		where Username = @Username
		and Authority = @UserGroupText
	end 

	if (@UserKey is not null)
	begin
		declare @Memberships nvarchar(250)
		select @Memberships=Memberships from xtcasUser where KeyLong = @UserKey

		-- Erstmal versuchen den String mit Komma zu entfernen und falls er dann noch da ist, den String ohne Komma suchen.
		select @Memberships = REPLACE(@Memberships, @UserGroupText+',', '')
		select @Memberships = REPLACE(@Memberships, @UserGroupText, '')
		
		update xtcasUser
		set Memberships = @Memberships
		where KeyLong = @UserKey
	end

return @@error