alter procedure dbo.spMinovaUpdateVersion (
	@Name nvarchar(255),
	@ModuleName nvarchar(255),
	@MajorVersion int,
	@MinorVersion int,
	@PatchLevel int,
	@BuildNumber int,
	@Dataname nvarchar(255) = null
) with encryption as
	if not exists (
		select * 
		from tVersion10 
		where KeyText = @Name
		  and ModuleName = @ModuleName
		  and coalesce(DataName,'') = coalesce(@Dataname, '')
	)
	begin
		insert into tVersion10(KeyText, ModuleName, MajorVersion, MinorVersion, PatchLevel, BuildNumber, Dataname) 
		values (@Name, @ModuleName, @MajorVersion, @MinorVersion, @PatchLevel, @BuildNumber,@Dataname)
	end
	else
	begin
		update tVersion10 set
			MajorVersion = @MajorVersion,
			MinorVersion = @MinorVersion,
			PatchLevel = @PatchLevel,
			BuildNumber = @BuildNumber,
			Dataname = @Dataname,
			LastUser = CURRENT_USER,
			LastDate = GETDATE(),
			lastAction = 2
		where KeyText = @Name
		  and ModuleName = @ModuleName
		  and coalesce(DataName,'') = coalesce(@Dataname, '')
		return 1 -- Ausführung des Scriptes erforderlich
	end
