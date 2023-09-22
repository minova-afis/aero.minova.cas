alter procedure dbo.xpcasReadServiceProperties (
	@KeyLong int output,
	@Service nvarchar(128) = null output,
    @Property nvarchar(256) = null output,
    @Val nvarchar(1024) = null output,
    @KeyText nvarchar(50) = null output
)
with encryption as
    select @Service = Service,
           @Property = Property,
           @Val = Val,
           @KeyText = KeyText
    from xtcasServiceProperties
    where KeyLong = @KeyLong

    return @@error