alter procedure dbo.xpcasReadUserGroupUserUsers (
	@KeyLong int,
	@UsersKey int = null output,
	@UserKey int = null output
)
with encryption as
	
	if(@UserKey is not null)
	begin
		select
		ug.KeyLong,
		us.KeyLong
		from xtcasUserGroup ug
		inner join xtcasAuthorities a on a.Authority=ug.KeyText
		inner join xtcasUsers us on a.Username = us.Username
		where ug.LastAction>0
		and a.LastAction>0
		and us.LastAction>0
	end
	
	if(@UserKey is not null)
	begin 
		select 
		ug.KeyLong,
		u.KeyLong
		from xtcasUserGroup ug 
		inner join xtcasUser u on (SELECT value FROM STRING_SPLIT( u.Memberships, ',') where value = ug.KeyText)=ug.KeyText
		where ug.LastAction>0
		and u.LastAction>0
	end

return @@error