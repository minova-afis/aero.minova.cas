alter procedure dbo.xpcasDeleteMdi (
	@KeyLong int
)
with encryption
as
	declare @ReturnCode int
	set @ReturnCode = 1

	update xtcasMdi
	set LastUser = dbo.xfCasUser(),
		LastDate = getDate(),
		LastAction = -1
	where KeyLong = @KeyLong

	return @ReturnCode
