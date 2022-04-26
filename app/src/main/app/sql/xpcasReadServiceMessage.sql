alter procedure dbo.xpcasReadServiceMessage (
	@KeyLong int,
	@ServiceURL varchar(50) output,
	@Port int output,
	@Message varchar(250) output,
	@isSent bit output,
	@NumberOfAttepts int output
)
with encryption as
	if (@KeyLong is not null)
	begin
		select	KeyLong,
				@ServiceURL = ServiceURL,
                @Port = Port,
				@Message = Message,
                @isSent = isSent,
                @NumberOfAttepts = NumberOfAttempts
		from xvcasCASServiceMessage
		where MessageKey = @KeyLong
		  and LastAction > 0
	end
return @@error