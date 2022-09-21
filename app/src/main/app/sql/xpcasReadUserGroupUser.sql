alter procedure dbo.xpcasReadUserGroupUser (
	@KeyLong int,
	@UserKey int
)
with encryption as
	
	select 
	ug.KeyLong,
	u.KeyLong as UserKey
	from xtcasUserGroup ug 
	inner join xtcasUser u on (SELECT value FROM STRING_SPLIT( u.Memberships, ',') where value = ug.KeyText)=ug.KeyText
	where 
	ug.KeyLong = @KeyLong
	and u.KeyLong=@KeyLong
	and ug.LastAction>0
	and u.LastAction>0

return @@error