alter procedure dbo.xpcasUpdateServiceMessage (
	@KeyLong int,
	@CASServiceKey int = null,
	@Message nvarchar(250) = null,
	@IsSent bit = null,
	@NumberOfAttempts int = null
)
with encryption as

update xtcasServiceMessage
set CASServiceKey = @CASServiceKey,
	Message = @Message,
	IsSent = @IsSent,
	NumberOfAttempts = @NumberOfAttempts
where KeyLong = @KeyLong
