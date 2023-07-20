alter procedure dbo.xpcasReadUsers (
	@KeyLong int output,
	@Username nvarchar(50) output,
	@Password nvarchar(100) = null output,
	@Description nvarchar(50) output
)
with encryption as

	select	@Username = Username,
	@Description = Description
	from xtcasUsers
	where KeyLong = @KeyLong
	return @@error