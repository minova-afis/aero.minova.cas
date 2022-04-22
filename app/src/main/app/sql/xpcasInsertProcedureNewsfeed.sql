alter procedure dbo.xpcasInsertProcedureNewsfeed (
    @KeyLong int output,
	@KeyText nvarchar(50),
	@Topic nvarchar(50)
)
with encryption as
	if exists(select * from xtcasProcedureNewsfeed
		where Keytext = @KeyText
          and Topic = @Topic
          and LastAction > 0 
		)
	begin
		raiserror('ADO | 25 | msg.DuplicateProcedureName | Der Prozedurname ist bereits mit dieser Tabelle verküpft!', 16, 1) with seterror
		return -1
	end

	if exists(select * from xtcasProcedureNewsfeed
		where KeyText = @KeyText
          and Topic = @Topic
          and LastAction < 0 
		)
	begin
    update xtcasProcedureNewsfeed
    set LastAction = 1
    where KeyText = @KeyText
      and Topic = @Topic
    end 
    else
    begin 
	insert into xtcasProcedureNewsfeed (
		KeyText,
		Topic
	) values (
		@KeyText,
		@Topic
	)
    end 
	select @KeyLong = @@identity
return @@error