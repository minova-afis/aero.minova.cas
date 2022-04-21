alter procedure dbo.xpcasInsertProcedureNewsfeed (
    @KeyLong int output,
	@KeyText nvarchar(50),
	@TableName nvarchar(50)
)
as
	if exists(select * from xtcasProcedureNewsfeed
		where Keytext = @KeyText
        and TableName = @TableName
        and LastAction > 0 
		)
	begin
		raiserror('ADO | 25 | msg.DuplicateProcedureName | Der Prozedurname ist bereits mit dieser Tabelle verküpft!', 16, 1) with seterror
		return -1
	end

	if exists(select * from xtcasProcedureNewsfeed
		where KeyText = @KeyText
        and TableName = @TableName
        and LastAction < 0 
		)
	begin
    update xtcasProcedureNewsfeed
    set LastAction = 1
    where KeyText = @KeyText
        and TableName = @TableName
    end 
    else
    begin 
	insert into xtcasProcedureNewsfeed (
		KeyText,
		TableName
	) values (
		@KeyText,
		@TableName
	)
    end 
	select @KeyLong = @@identity
return @@error