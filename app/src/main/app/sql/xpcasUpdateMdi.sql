alter procedure dbo.xpcasUpdateMdi (
	@KeyLong int output,
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
	declare @ReturnCode int
	set @ReturnCode = 1

	update xtcasMdi
	set ID = @ID,
		Icon = @Icon,
		Label = @Label,
		Menu = @Menu,
		Position = @Position,
		SecurityToken = @SecurityToken,
		MdiTypeKey = @MdiTypeKey,
		LastUser = dbo.xfCasUser(),
		LastDate = getDate(),
		LastAction = 2
	where KeyLong = @KeyLong

	return @ReturnCode
