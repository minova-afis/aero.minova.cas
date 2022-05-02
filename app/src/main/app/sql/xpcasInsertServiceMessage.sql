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
	IsSent
) values (
	@CASServiceKey,
	@Message,
	getDate(),
	0
)
select @KeyLong = @@identity
return @@error