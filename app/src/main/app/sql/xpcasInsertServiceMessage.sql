alter procedure dbo.xpcasInsertServiceMessage (
	@KeyLong int output,
	@CASServiceKey int,
	@Message nvarchar(250)
)
with encryption as

insert into xtcasServiceMessage (
	CASServiceKey,
	Message
) values (
	@CASServiceKey,
	@Message
)
select @KeyLong = @@identity
return @@error