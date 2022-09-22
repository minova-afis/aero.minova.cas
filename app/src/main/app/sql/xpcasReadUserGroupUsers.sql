alter procedure dbo.xpcasReadUserGroupUsers (
	@KeyLong int,
	@UsersKey int
)
with encryption as

	select
		ug.KeyLong,
		us.KeyLong as UsersKey
	from xtcasUserGroup ug
	inner join xtcasAuthorities a on a.Authority=ug.KeyText
	inner join xtcasUsers us on a.Username = us.Username
	where 
	ug.KeyLong = @KeyLong
	and ug.LastAction>0
	and a.LastAction>0
	and us.LastAction>0

	return @@error