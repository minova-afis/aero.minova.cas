alter procedure dbo.spMinovaCheckData (
	@ProcedureName nvarchar(255),
	@ModuleName nvarchar(255),
	@MajorVersion int,
	@MinorVersion int,
	@PatchLevel int,
	@BuildNumber int,
	@Dataname nvarchar(255) = null
) with encryption as

SET NOCOUNT ON -- Verhindert, dass mehrere Ergebnisse zurückgegen werden. Ist vor allem ein Problem bei Pointern. 
select @ProcedureName = 'DATA'
	if not exists (select * from tVersion10 where Dataname = @Dataname and ModuleName = @ModuleName and KeyText = 'DATA')
	begin
		insert into tVersion10(KeyText, ModuleName, MajorVersion, MinorVersion, PatchLevel, BuildNumber, Dataname) 
		values (@ProcedureName, @ModuleName, @MajorVersion, @MinorVersion, @PatchLevel, @BuildNumber,@Dataname)
		return 5 -- Ausführung des Scriptes erforderlich
	end
	
	if not exists (
		select * 
		from tVersion10 
		where KeyText = @ProcedureName
		  and ModuleName = @ModuleName
		  and MajorVersion = @MajorVersion
		  and MinorVersion = @MinorVersion
		  and PatchLevel = @PatchLevel
		  and BuildNumber = @BuildNumber
		  and Dataname = @Dataname
	)
	begin
		return 5 -- Ausführung des Scriptes erforderlich
	end
	
	return 5 -- Ausführung nicht zwingend erforderlich
