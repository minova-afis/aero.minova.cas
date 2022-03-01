alter procedure dbo.xpcasInsertAllPrivilegesToUserGroup (
	@UserGroup nvarchar(50),
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

    if not exists (select * from xtcasUserGroup where KeyText = @UserGroup)
    begin
        insert into xtcasUserGroup (KeyText,SecurityToken) VALUES (@UserGroup, '#' + @SecurityToken)
    end



    CREATE TABLE #temp 
    (  
        KeyText NVARCHAR(50)  
    );  

    insert into #temp select KeyLong from xtCasUserPrivilege

    declare @UserGroupKey int,
    @UserPrivilegeKey int

    select @UserGroupKey = KeyLong from xtcasUserGroup where KeyText = @UserGroup

    DECLARE temp_cursor CURSOR FOR SELECT KeyText FROM #temp

    open temp_cursor
    fetch NEXT from temp_cursor into @UserPrivilegeKey


    WHILE @@FETCH_STATUS = 0  
        BEGIN 

        if not exists (select * from xtcasLuUserPrivilegeUserGroup where UserPrivilegeKey = @UserPrivilegeKey and UserGroupKey=@UserGroupKey)
            begin
            insert into xtcasLuUserPrivilegeUserGroup (UserPrivilegeKey,UserGroupKey) values (@UserPrivilegeKey, @UserGroupKey )
            end

        fetch NEXT from temp_cursor into @UserPrivilegeKey
        END 

    CLOSE temp_cursor
    DEALLOCATE temp_cursor 

    drop table #temp