alter procedure dbo.xpcasInsertMdi (
	@KeyLong int output,
	@KeyText nvarchar(50),
	@Icon nvarchar(100),
	@Label nvarchar(100),
	@Menu nvarchar(100),
	@Position float,
	@SecurityToken nvarchar(50),
	@MdiTypeKey int,
	@Modulname nvarchar(500)
)
with encryption
as
	declare @ReturnCode int
	set @ReturnCode = 1

	if not exists (select * from xtcasMdi where LastAction >-1 and KeyText = @KeyText)
	begin
		insert into xtcasMdi(
			KeyText,
			Icon,
			Label,
			Menu,
			Position,
			SecurityToken,
			MdiTypeKey,
			Modulname,
			LastUser,
			LastDate,
			LastAction
		) values (
			@KeyText,
			@Icon,
			@Label,
			@Menu,
			@Position,
			@SecurityToken,
			@MdiTypeKey,
			@Modulname,
			dbo.xfCasUser(),
			getDate(),
			1
		)
		select @KeyLong = @@IDENTITY
	end
	else
	begin
		raiserror('ADO | 25 | msg.DuplicateMatchcodeNotAllowed | Doppelter Matchcode nicht erlaubt.', 16, 1)
		return -1
	end

	return @ReturnCode
