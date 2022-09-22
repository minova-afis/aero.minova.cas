alter procedure dbo.xpcasDeleteUserGroupUser (
	@KeyLong int,
	@UserKey int output
)
with encryption as

	declare
	@UserGroupText nvarchar(50)
	select @UserGroupText=UserCode from xtcasUserGroup where KeyLong=@KeyLong


	declare @Memberships nvarchar(250)
	select @Memberships=Memberships from xtcasUser where KeyLong = @UserKey

	-- Erstmal versuchen den String mit Raute zu entfernen und falls er dann noch da ist, den String ohne Raute suchen.
	select @Memberships = REPLACE(@Memberships, @UserGroupText+'#', '')
	select @Memberships = REPLACE(@Memberships, @UserGroupText, '')
		
	update xtcasUser
	set Memberships = @Memberships
	where KeyLong = @UserKey

return @@error