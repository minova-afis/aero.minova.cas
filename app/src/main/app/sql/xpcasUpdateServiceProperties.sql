alter procedure dbo.xpcasUpdateServiceProperties (
	@KeyLong int,
    @Service nvarchar(128) = null,
	@Property nvarchar(256) = null,
	@Val nvarchar(1024) = null
)
with encryption as
	update xtcasServiceProperties
	set Service = @Service,
        Property = @Property,
        Val = @Val
	where KeyLong = @KeyLong

    return @@error