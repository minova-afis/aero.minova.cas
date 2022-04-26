alter procedure dbo.xpcasDeleteServiceMessage (
    @KeyLong int
)
with encryption as

update xtcasServiceMessage
set 
    LastAction = -1,
    LastUser = dbo.xfCasUser(),
    LastDate = getDate()
where 
    KeyLong = @KeyLong