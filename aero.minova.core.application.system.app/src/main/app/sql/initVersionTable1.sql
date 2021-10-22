declare @ProcedureName nvarchar(255)
declare @ModuleName nvarchar(255)
declare @Dataname nvarchar(255)

select @ProcedureName = 'spMinovaCheckProcedure'
select @ModuleName = 'ch.minova.install'
select @Dataname = 'ch.minova.install.jar'

if not exists (select * from tVersion10 where KeyText = @ProcedureName and ModuleName = @ModuleName)
begin
	if not exists (select * from INFORMATION_SCHEMA.ROUTINES where SPECIFIC_NAME = @ProcedureName)
	begin
		exec('create Procedure dbo.' + @ProcedureName + N' as select 0')
	end
	insert into tVersion10(KeyText, ModuleName, MajorVersion, MinorVersion, PatchLevel, BuildNumber, Dataname) 
	values (@ProcedureName, @ModuleName, 10, 0, 0, -1, @Dataname)
end



select @ProcedureName = 'spMinovaUpdateVersion'

if not exists (select * from tVersion10 where KeyText = @ProcedureName and ModuleName = @ModuleName)
begin
	if not exists (select * from INFORMATION_SCHEMA.ROUTINES where SPECIFIC_NAME = @ProcedureName)
	begin
		exec('create Procedure dbo.' + @ProcedureName + N' as select 0')
	end
	insert into tVersion10(KeyText, ModuleName, MajorVersion, MinorVersion, PatchLevel, BuildNumber,Dataname) 
	values (@ProcedureName, @ModuleName, 10, 0, 0, -1, @Dataname)
end
