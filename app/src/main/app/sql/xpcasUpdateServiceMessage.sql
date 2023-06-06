alter procedure dbo.xpcasUpdateServiceMessage (
	@KeyLong int,
	@IsSent bit = null,
	@NumberOfAttempts int = null
)
with encryption as

update xtcasServiceMessage
set IsSent = @IsSent,
	NumberOfAttempts = @NumberOfAttempts,
	LastAction = 2,
	LastDate = getDate(),
	LastUser = dbo.xfCasUser()
where KeyLong = @KeyLong
