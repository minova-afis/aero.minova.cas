alter procedure dbo.xpcasInsertAllPrivilegesToAdmin 
as

DECLARE
@UserGroupKey int,
@UserPrivilegeKey int

IF OBJECT_ID('tempDB..#temp', 'U') IS NOT NULL
DROP TABLE #temp

SELECT tu.KeyLong
INTO #temp
FROM xtCasUserPrivilege tu
left join xtcasLuUserPrivilegeUserGroup x on x.UserPrivilegeKey = tu.KeyLong
where x.KeyLong is null

select @UserGroupKey = KeyLong from xtcasUserGroup where KeyText = 'admin'

DECLARE temp_cursor CURSOR FOR 
SELECT KeyLong FROM #temp

FETCH NEXT FROM temp_cursor INTO @UserPrivilegeKey  

WHILE @@FETCH_STATUS = 0  
BEGIN  
      exec xpcasUpdateLuUserPrivilegeUserGroup null, @UserPrivilegeKey, @UserGroupKey, null
      FETCH NEXT FROM temp_cursor INTO @UserPrivilegeKey 
END 

CLOSE temp_cursor  
DEALLOCATE temp_cursor 