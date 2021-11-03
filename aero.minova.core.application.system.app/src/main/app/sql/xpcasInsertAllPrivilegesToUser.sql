alter procedure dbo.xpcasInsertAllPrivilegesToUser (
	@UserName nvarchar(50),
	@SecurityToken nvarchar(10)
) with encryption as
    if not exists (select * from xtcasUserPrivilege where KeyText = 'setup')
    begin
        insert into xtcasUserPrivilege (KeyText) values ('setup')
    end

    insert into xtcasUserPrivilege (
        KeyText)
    select r.SPECIFIC_NAME
    from INFORMATION_SCHEMA.ROUTINES r
    left join xtcasUserPrivilege p on p.KeyText = r.SPECIFIC_NAME
    where p.KeyLong is null

    insert into xtCasUserPrivilege (
        KeyText)
    select t.TABLE_NAME
    from INFORMATION_SCHEMA.TABLES t
    left join xtcasUserPrivilege p on p.KeyText = t.TABLE_NAME
    where p.KeyText is null

    if not exists (select * from xtcasUserGroup where KeyText = @UserName)
    begin
        insert into xtcasUserGroup (KeyText,SecurityToken) VALUES (@UserName, '#' + @SecurityToken)
    end

    insert into xtcasLuUserPrivilegeUserGroup (
        UserPrivilegeKey,
        UserGroupKey
    ) select up.KeyLong,
        (select cast(KeyLong as nvarchar) from xtcasUserGroup where KeyText = @UserName)
    from xtcasUserPrivilege up
    left join xtcasLuUserPrivilegeUserGroup lp on lp.UserPrivilegeKey = up.KeyLong
    where lp.KeyLong is null
