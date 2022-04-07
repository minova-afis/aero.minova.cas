if not exists (select * from INFORMATION_SCHEMA.TABLES where TABLE_NAME = 'tVersion10')
begin
	create table tVersion10 (
		KeyLong int identity(1, 1),
		KeyText nvarchar(255),
		ModuleName nvarchar(255) null,
		MajorVersion int,
		MinorVersion int,
		PatchLevel int,
		BuildNumber bigint,
		Dataname nvarchar(255),
		LastUser nvarchar(255) null constraint DF_tVersion10_LastUser default(current_user),
		LastDate datetime null constraint DF_tVersion10_LastDate default(getdate()),
		LastAction int null constraint DF_tVersion10_LastAction default(1)
	)
end
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_NAME = 'tVersion10' AND COLUMN_NAME = 'Dataname')
BEGIN
   ALTER TABLE [tVersion10] ADD [Dataname] nvarchar(255)
END