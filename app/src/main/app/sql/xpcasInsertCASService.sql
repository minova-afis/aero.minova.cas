alter procedure dbo.xpcasInsertCASService (
	@KeyLong int output,
	@KeyText nvarchar(50),
	@ServiceURL nvarchar(50),
    @Port int,
	@ServiceMessageReceiverLoginTypeKey int,
	@ClientID nvarchar(50),
	@ClientSecret nvarchar(250),
	@TokenURL nvarchar(250)
)
with encryption as
if exists(select * from xtcasCASServices
	where KeyText = @KeyText
      and ServiceURL = @ServiceURL
      and Port = @Port
      and LastAction > 0 
	)
begin
	raiserror('ADO | 25 | msg.DuplicateService | Es besteht bereits ein Dienst mit diesen Parametern!', 16, 1) with seterror
	return -1
end

insert into xtcasCASServices (
	KeyText,
	ServiceURL,
	Port,
	ServiceMessageReceiverLoginTypeKey,
  	ClientID,
  	ClientSecret,
    TokenURL
) values (
	@KeyText,
	@ServiceURL,
	@Port,
	@ServiceMessageReceiverLoginTypeKey,
 	@ClientID,
  	@ClientSecret,
    @TokenURL
)
select @KeyLong = @@identity

return @@error