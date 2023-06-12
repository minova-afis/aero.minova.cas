alter procedure dbo.xpcasInsertServiceMessage (
	@KeyLong int output,
	@CASServiceKey int,
	@Message nvarchar(1024)
)
with encryption as

insert into xtcasServiceMessage (
	CASServiceKey,
	Message,
	MessageCreationDate,
	IsSent,
	LastAction,
	LastDate,
	LastUser
) values (
	@CASServiceKey,
	@Message,
	getDate(),
	0,
	1,
	getDate(),
	dbo.xfCasUser()
)
select @KeyLong = @@identity
return @@error