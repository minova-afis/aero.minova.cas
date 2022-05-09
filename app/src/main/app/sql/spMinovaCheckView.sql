alter procedure spMinovaCheckView
(
	@ViewName nvarchar(255),
	@ModuleName nvarchar(255),
	@MajorVersion int,
	@MinorVersion int,
	@PatchLevel int,
	@BuildNumber int,
	@Dataname nvarchar(255) = null
) with encryption as
SET NOCOUNT ON
	if not exists (select * from tVersion10 where KeyText = @ViewName and ModuleName = @ModuleName)
	begin
		exec('create view dbo.' + @ViewName + N' as select 0 as col1')
--		insert into tVersion10(KeyText, ModuleName, MajorVersion, MinorVersion, PatchLevel, BuildNumber) 
--		values (@ViewName, @ModuleName, @MajorVersion, @MinorVersion, @PatchLevel, @BuildNumber)
		return 2 -- Ausführung des Scriptes erforderlich
	end
	
	if not exists (
		select * 
		from tVersion10 
		where KeyText = @ViewName
		  and ModuleName = @ModuleName
		  and MajorVersion = @MajorVersion
		  and MinorVersion = @MinorVersion
		  and PatchLevel = @PatchLevel
		  and BuildNumber = @BuildNumber
		  and Dataname = @Dataname
	)
	begin
	/* Die Versionsinformationen werden seit 17.03.2014 in der Prozedur spMinovaUpdateVersion gesetzt
		update tVersion10 set
			MajorVersion = @MajorVersion,
			MinorVersion = @MinorVersion,
			PatchLevel = @PatchLevel,
			BuildNumber = @BuildNumber,
			LastUser = CURRENT_USER,
			LastDate = GETDATE(),
			lastAction = 2
		where KeyText = @ViewName
		  and ModuleName = @ModuleName
	*/
		return 2 -- Ausführung des Scriptes erforderlich
	end
	
	return 1 -- Ausführung nicht zwingend erforderlich
