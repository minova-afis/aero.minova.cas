alter procedure dbo.xpcasInsertServiceMessage (
	@KeyLong int output,
	@CASServiceKey int,
	@Message nvarchar(250),
	@MessageCreationDate datetime
)
with encryption as

insert into xtcasServiceMessage (
	CASServiceKey,
	Message,
	MessageCreationDate
) values (
	@CASServiceKey,
	@Message,
	@MessageCreationDate
)
select @KeyLong = @@identity
return @@error