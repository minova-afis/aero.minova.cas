alter procedure dbo.xpcasUpdateServiceMessage (
	@KeyLong int,
	@IsSent bit = null,
	@NumberOfAttempts int = null,
	
)
with encryption as

update xtcasServiceMessage
set IsSent = @IsSent,
	NumberOfAttempts = @NumberOfAttempts
where KeyLong = @KeyLong
