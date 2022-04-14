create procedure dbo.xpcasInsertNewsfeedListener (
    @KeyLong int output,
	@CASServiceKey int,
	@TableName nvarchar(50)
)
as
	if exists(select * from xtcasNewsfeedListener
		where CASServiceKey = @CASServiceKey
        and TableName = @TableName
        and LastAction > 0 
		)
	begin
		raiserror('ADO | 25 | msg.DuplicateNewsfeedListener | Es besteht bereits ein NewsfeedListener mit diesen Parametern!', 16, 1) with seterror
		return -1
	end

	if exists(select * from xtcasNewsfeedListener
		where CASServiceKey = @CASServiceKey
        and TableName = @TableName
        and LastAction < 0 
		)
	begin
    update xtcasNewsfeedListener
    set LastAction = 1
    where CASServiceKey = @CASServiceKey
        and TableName = @TableName
    end 
    else
    begin 
	insert into xtcasNewsfeedListener (
		CASServiceKey,
		TableName
	) values (
		@CASServiceKey,
		@TableName
	)
    end 
	select @KeyLong = @@identity
return @@error