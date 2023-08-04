alter procedure dbo.xpcasInsertServiceMessage (
	@KeyLong int output,
	@CASServiceKey int,
	@Message nvarchar(1024),
	@KeyText nvarchar (50) = null
)
with encryption as

insert into xtcasServiceMessage (
	CASServiceKey,
	Message,
	MessageCreationDate,
	IsSent,
	KeyText,
	LastAction,
	LastDate,
	LastUser
) values (
	@CASServiceKey,
	@Message,
	@KeyText,
	getDate(),
	0,
	1,
	getDate(),
	dbo.xfCasUser()
)
select @KeyLong = @@identity
return @@error