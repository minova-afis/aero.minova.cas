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

-- Einträge älter als 30 Tage löschen
delete from xtcasServiceMessage
where MessageCreationDate < dateadd(day,-30,getDate());