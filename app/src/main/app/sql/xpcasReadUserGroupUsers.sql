alter procedure dbo.xpcasReadUserGroupUsers (
	@KeyLong int,
	@UsersKey int output
)
with encryption as

		select
		ug.KeyLong,
		us.KeyLong
		from xtcasUserGroup ug
		inner join xtcasAuthorities a on a.Authority=ug.KeyText
		inner join xtcasUsers us on a.Username = us.Username
		where 
		ug.KeyLong = 2
		and ug.LastAction>0
		and a.LastAction>0
		and us.LastAction>0

return @@error