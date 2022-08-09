alter procedure dbo.xpcasReadAuthorities (
	@KeyLong int output,
	@Username nvarchar(50) output
)
with encryption as
	select a.KeyLong,
		Username,
		ug.KeyLong as Authority
		from xtcasAuthorities a
		left join xtcasUserGroup ug on a.Authority=ug.KeyText
	where Username = @Username

return @@error