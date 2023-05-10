alter procedure dbo.xpcasInsertCASService (
	@KeyLong int output,
	@KeyText nvarchar(50),
	@ServiceURL nvarchar(50),
    @Port int,
	@ServiceMessageReceiverLoginTypeKey int
)
with encryption as
if exists(select * from xtcasCASServices
	where KeyText = @KeyText
      and ServiceURL = @ServiceURL
      and Port = @Port
	  and ServiceMessageReceiverLoginTypeKey = @ServiceMessageReceiverLoginTypeKey
      and LastAction > 0 
	)
begin
	raiserror('ADO | 25 | msg.DuplicateService | Es besteht bereits ein Dienst mit diesen Parametern!', 16, 1) with seterror
	return -1
end

if exists(select * from xtcasCASServices
		where KeyText = @KeyText
          and ServiceURL = @ServiceURL
          and Port = @Port
		  and ServiceMessageReceiverLoginTypeKey = @ServiceMessageReceiverLoginTypeKey
          and LastAction < 0 
		)
begin
	select @KeyLong= KeyLong 
	from xtcasCASServices 
    where KeyText = @KeyText
      and ServiceURL = @ServiceURL
      and Port = @Port
	  and ServiceMessageReceiverLoginTypeKey = @ServiceMessageReceiverLoginTypeKey

    update xtcasCASServices
    set LastAction = 1
	where KeyLong=@KeyLong
end 
else
begin 
	insert into xtcasCASServices (
		KeyText,
		ServiceURL,
		Port,
		ServiceMessageReceiverLoginTypeKey
	) values (
		@KeyText,
		@ServiceURL,
		@Port,
		@ServiceMessageReceiverLoginTypeKey
	)
	select @KeyLong = @@identity
end 
return @@error