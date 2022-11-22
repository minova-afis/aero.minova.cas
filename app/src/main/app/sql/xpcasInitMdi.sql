alter procedure dbo.xpcasInitMdi (
	@ID nvarchar(100),
	@Icon nvarchar(100),
	@Label nvarchar(100),
	@Menu nvarchar(100),
	@Position float,
	@SecurityToken nvarchar(50),
	@MdiTypeKey int
)
with encryption
as

	if not exists (select * from xtcasMdi where LastAction >-1 and ID = @ID)
	begin
		insert into xtcasMdi(
			ID,
			Icon,
			Label,
			Menu,
			Position,
			SecurityToken,
			MdiTypeKey,
			LastUser,
			LastDate,
			LastAction
		) values (
			@ID,
			@Icon,
			@Label,
			@Menu,
			@Position,
			@SecurityToken,
			@MdiTypeKey,
			dbo.xfCasUser(),
			getDate(),
			1
		)
	end
