alter procedure dbo.xpcasDeleteAuthorities (
	@KeyLong int,
	@Username nvarchar(50) = null,
	@Authority nvarchar(50) = null
)
with encryption as

	delete from xtcasAuthorities
	where KeyLong = @KeyLong

	return @@error