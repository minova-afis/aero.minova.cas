alter procedure dbo.xpcasReadUserPrivilege (
	@KeyLong int output,
	@KeyText nvarchar(200) = null output,
	@Description nvarchar(50) = null output
)
with encryption as
	if (@KeyLong is null)
	begin
		select @KeyLong = KeyLong from xtcasUserPrivilege where KeyText = @KeyText and LastAction > 0
	end

	select	@KeyText = KeyText,
			@Description = Description
	from xtcasUserPrivilege
	where KeyLong = @KeyLong
return @@error