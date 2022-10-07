alter procedure dbo.xpcasInsertUserGroupUser (
	@KeyLong int output,
    @UserKey int
) with encryption as

    declare @UserGroupToken nvarchar(50)
    declare @Memberships nvarchar(250)

    -- Den KeyText der UserGroup herausbekommen.
    select @UserGroupToken='#'+KeyText from xtcasUserGroup where KeyLong=@KeyLong

    -- Überprüfen, ob es innerhalb der Memberships eine Membership der UserGroup gibt.
    select @Memberships=Memberships from xtcasUser where KeyLong=@UserKey

    declare @UserGroupIsInMembership int

    select @UserGroupIsInMembership = CHARINDEX(@UserGroupToken, @Memberships) 

    if (@UserGroupIsInMembership = 0)
    begin
        select @Memberships = CONCAT(@Memberships, @UserGroupToken );

        update xtcasUser
        set Memberships = @Memberships
        where KeyLong = @UserKey 
    end

    select @KeyLong = @@identity
    return @@error