alter procedure dbo.xpcasUpdateUserGroupUser (
	@KeyLong int output,
	@UserKey int
)
with encryption as
    --DO NOTHING
return @KeyLong