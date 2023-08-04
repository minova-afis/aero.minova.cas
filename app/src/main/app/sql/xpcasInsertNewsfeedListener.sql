alter procedure dbo.xpcasInsertNewsfeedListener (
    @KeyLong int output,
	@CASServiceKey int,
	@Topic nvarchar(50),
	@KeyText nvarchar(50) = null
)
with encryption as
if exists(select * from xtcasNewsfeedListener
	where CASServiceKey = @CASServiceKey
      and Topic = @Topic
      and LastAction > 0 
	)
begin
	raiserror('ADO | 25 | msg.DuplicateNewsfeedListener | Es besteht bereits ein NewsfeedListener mit diesen Parametern!', 16, 1) with seterror
	return -1
end

if exists(select * from xtcasNewsfeedListener
	where CASServiceKey = @CASServiceKey
      and Topic = @Topic
      and LastAction < 0 
	)
begin
    update xtcasNewsfeedListener
    set LastAction = 1,
		LastUser = dbo.xfCasUser(),
		LastDate = getDate()
    where CASServiceKey = @CASServiceKey
      and Topic = @Topic
end 
else
begin 
	insert into xtcasNewsfeedListener (
		CASServiceKey,
		Topic,
		KeyText,
		LastAction,
		LastDate,
		LastUser
	) values (
		@CASServiceKey,
		@Topic,
		@KeyText,
		1,
		getDate(),
		dbo.xfCasUser()
	)
	select @KeyLong = @@identity
end
return @@error