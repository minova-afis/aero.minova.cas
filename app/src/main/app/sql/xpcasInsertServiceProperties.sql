alter procedure dbo.xpcasInsertServiceProperties (
	@KeyLong int output,
	@Service nvarchar(128) = null,
	@Property nvarchar(256) = null,
	@Val nvarchar(1024) = null,
	@KeyText nvarchar(50) = null
)
with encryption as
	insert into xtcasServiceProperties (
		Service,
		Property,
		Val,
		KeyText,
		LastAction,
		LastDate,
		LastUser
	) values (
		@Service,
		@Property,
		@Val,
		@KeyText,
		1,
		getDate(),
		dbo.xfCasUser()
	)

	select @KeyLong = @@identity

    return @@error
