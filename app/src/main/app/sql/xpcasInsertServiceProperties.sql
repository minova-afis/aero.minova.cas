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
		Val,
		LastAction,
		LastDate,
		LastUser
	) values (
		@Service,
		@Property,
		@Val,
		1,
		getDate(),
		dbo.xfCasUser()
	)

	select @KeyLong = @@identity

    return @@error
