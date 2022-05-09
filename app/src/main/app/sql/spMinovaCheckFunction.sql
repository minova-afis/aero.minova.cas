alter procedure spMinovaCheckFunction
(
	@FunctionName nvarchar(255),
	@ModuleName nvarchar(255),
	@MajorVersion int,
	@MinorVersion int,
	@PatchLevel int,
	@BuildNumber int
) with encryption as
SET NOCOUNT ON
	if not exists (select * from tVersion10 where KeyText = @FunctionName and ModuleName = @ModuleName)
	begin
		exec('create function dbo.' + @FunctionName + N'(@a int) returns int as begin return 0 end')
--		insert into tVersion10(KeyText, ModuleName, MajorVersion, MinorVersion, PatchLevel, BuildNumber) 
--		values (@FunctionName, @ModuleName, @MajorVersion, @MinorVersion, @PatchLevel, @BuildNumber)
		return 2 -- Ausführung des Scriptes erforderlich
	end
	
	if not exists (
		select * 
		from tVersion10 
		where KeyText = @FunctionName
		  and ModuleName = @ModuleName
		  and MajorVersion = @MajorVersion
		  and MinorVersion = @MinorVersion
		  and PatchLevel = @PatchLevel
		  and BuildNumber = @BuildNumber
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
		where KeyText = @FunctionName
		  and ModuleName = @ModuleName
	*/
		return 2 -- Ausführung des Scriptes erforderlich
	end
	
	return 1 -- Ausführung nicht zwingend erforderlich
