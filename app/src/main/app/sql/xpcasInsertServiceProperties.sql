alter procedure dbo.xpcasInsertServiceProperties (
	@KeyLong int output,
	@Service nvarchar(128) = null,
	@Property nvarchar(256) = null,
	@Val nvarchar(1024) = null
)
with encryption as
	insert into xtcasServiceProperties (
		Service,
		Property,
		Val
	) values (
		@Service,
		@Property,
		@Val
	)

	select @KeyLong = @@identity

    return @@error
