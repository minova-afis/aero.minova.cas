alter function dbo.xfCasUser()
returns nvarchar(100) as
begin
	return (select COALESCE(CONVERT(nvarchar(100), (select session_context(N'casUser'))), 'unknown-user'))
end