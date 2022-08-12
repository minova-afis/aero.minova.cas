alter procedure dbo.xpcasUpdateUserGroupUserUsers (
	@KeyLong int output,
	@UsersKey int = null,
	@UserKey int = null
)
with encryption as
    --DO NOTHING
return @KeyLong