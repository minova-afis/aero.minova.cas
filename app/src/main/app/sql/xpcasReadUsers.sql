alter procedure dbo.xpcasReadUsers (
	@KeyLong int output,
	@Username nvarchar(50) output,
	@Password nvarchar(100) = null output
)
with encryption as

	select	@Username = Username
	from xtcasUsers
	where KeyLong = @KeyLong
return @@error