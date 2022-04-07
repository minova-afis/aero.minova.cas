alter procedure dbo.xpcasReadUserGroup (
	@KeyLong int output,
	@KeyText nvarchar(50) = null output,
	@Description nvarchar(50) = null output,
	@UserCode nvarchar(50) = null output,
	@SecurityToken nvarchar(250) = null output
)
with encryption as
	if (@KeyLong is null)
	begin
		select @KeyLong = KeyLong from xtcasUserGroup where KeyText = @KeyText and LastAction > 0
	end

	select	@KeyText = KeyText,
			@Description = Description,
			@UserCode = UserCode,
			@SecurityToken = SecurityToken
	from xtcasUserGroup
	where KeyLong = @KeyLong
return @@error