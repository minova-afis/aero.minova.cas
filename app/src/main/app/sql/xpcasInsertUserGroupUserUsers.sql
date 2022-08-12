alter procedure dbo.xpcasInsertUserGroupUserUsers (
	@KeyLong int output,
	@UsersKey int = null,
    @UserKey int = null
) with encryption as

    declare @UserGroupToken nvarchar(50)
    declare @Memberships nvarchar(250)

    -- Den Namen der UserGroup herausbekommen.
    select @UserGroupToken=KeyText from xtcasUserGroup where KeyLong=@KeyLong

    if (@UserKey is not null)
    begin 

        -- Überprüfen, ob es innerhalb der Memberships eine Membership der U
        select @Memberships=Memberships from xtcasUser where KeyLong=@UserKey

        declare @UserGroupIsInMembership int

        select @UserGroupIsInMembership = CHARINDEX(@UserGroupToken, @Memberships) 

        if (@UserGroupIsInMembership = 0)
        begin
            select @Memberships = CONCAT(@Memberships, ',', @UserGroupToken );

            update xtcasUser
            set Memberships = @Memberships
            where KeyLong = @UserKey 
        end
    end
    
    if(@UsersKey is not null)
    begin 
        exec xpcasInsertAuthorities null, @UsersKey, @KeyLong
    end

select @KeyLong = @@identity
return @@error