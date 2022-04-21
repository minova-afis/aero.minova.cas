alter procedure dbo.xpcasInsertCASService (
	@KeyLong int output,
	@KeyText nvarchar(50),
	@ServiceURL nvarchar(50),
    @Port int
)
as
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

if exists(select * from xtcasCASServices
		where KeyText = @KeyText
        and ServiceURL = @ServiceURL
        and Port = @Port
        and LastAction < 0 
		)
	begin
	select @KeyLong= KeyLong 
	from xtcasCASServices 
    where KeyText = @KeyText
        and ServiceURL = @ServiceURL
        and Port = @Port 

    update xtcasCASServices
    set LastAction = 1
	where KeyLong=@KeyLong
    end 
    else
    begin 
	insert into xtcasCASServices (
		KeyText,
		ServiceURL,
		Port
	) values (
		@KeyText,
		@ServiceURL,
		@Port
	)
	select @KeyLong = @@identity
    end 
return @@error