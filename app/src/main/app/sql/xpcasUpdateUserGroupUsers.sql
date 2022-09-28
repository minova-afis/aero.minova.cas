alter procedure dbo.xpcasUpdateUserGroupUsers (
	@KeyLong int output,
	@UsersKey int
)
with encryption as
    --DO NOTHING
	return @KeyLong