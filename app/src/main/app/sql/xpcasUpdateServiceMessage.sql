alter procedure dbo.xpcasUpdateServiceMessage (
	@KeyLong int,
	@IsSent bit = null,
	@NumberOfAttempts int = null,
	@Failed bit = 0
)
with encryption as

update xtcasServiceMessage
set IsSent = @IsSent,
	NumberOfAttempts = @NumberOfAttempts,
	Failed = @Failed,
	LastAction = 2,
	LastDate = getDate(),
	LastUser = dbo.xfCasUser()
where KeyLong = @KeyLong
