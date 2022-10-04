alter procedure dbo.xpcasDeleteUserGroupUser (
	@KeyLong int,
	@UserKey int output
)
with encryption as

	declare
	@UserGroupText nvarchar(50)
	select @UserGroupText=KeyText from xtcasUserGroup where KeyLong=@KeyLong


	declare @Memberships nvarchar(250)
	select @Memberships=Memberships from xtcasUser where KeyLong = @UserKey

	-- Wir können den String nur dann löschen, wenn er auch eine Raute davor hat, sonst löschen wir möglicherweise Substrings.
	select @Memberships = REPLACE(@Memberships, @UserGroupText+'#', '')
		
	update xtcasUser
	set Memberships = @Memberships
	where KeyLong = @UserKey

	return @@error