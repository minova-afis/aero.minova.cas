alter procedure dbo.xpcasReadMdi (
	@KeyLong int output,
	@KeyText nvarchar(50) output,
	@Icon nvarchar(100) output,
	@Label nvarchar(100) output,
	@Menu nvarchar(100) output,
	@Position float output,
	@SecurityToken nvarchar(50) output,
	@MdiTypeKey int output
)
with encryption
as
	select @KeyLong = KeyLong,
		@KeyText = KeyText,
		@Icon = Icon,
		@Label = Label,
		@Menu = Menu,
		@Position = Position,
		@SecurityToken = SecurityToken,
		@MdiTypeKey = MdiTypeKey
	from xtcasMdi
	where KeyLong = @KeyLong and
		LastAction > -1