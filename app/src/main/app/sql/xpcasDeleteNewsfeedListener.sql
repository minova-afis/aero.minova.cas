alter procedure dbo.xpcasDeleteNewsfeedListener (
    @KeyLong int
)
with encryption as

update xtcasNewsfeedListener
set 
    LastAction = -1,
    LastUser = dbo.xfCasUser(),
    LastDate = getDate()
where 
    KeyLong = @KeyLong