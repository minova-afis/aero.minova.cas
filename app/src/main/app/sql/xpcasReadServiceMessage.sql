alter procedure dbo.xpcasReadServiceMessage (
	@KeyLong int,
	@ServiceURL varchar(50) output,
	@Port int output,
	@Message varchar(1024) output,
	@isSent bit output,
	@NumberOfAttepts int output,
	@MessageCreationDate datetime output,
	@KeyText nvarchar(50) = null output
)
with encryption as
if (@KeyLong is not null)
begin
		select	@ServiceURL = ServiceURL,
                @Port = Port,
				@Message = Message,
                @isSent = isSent,
                @NumberOfAttepts = NumberOfAttempts,
				@MessageCreationDate = MessageCreationDate,
				@KeyText = KeyText
		from xvcasCASServiceMessage
		where MessageKey = @KeyLong
end
return @@error