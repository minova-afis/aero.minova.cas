alter procedure dbo.xpcasDeleteProcedureNewsfeed (
    @KeyLong int
)
with encryption as

update xtcasProcedureNewsfeed
set 
    LastAction = -1,
    LastUser = dbo.xfCasUser(),
    LastDate = getDate()
where 
    KeyLong = @KeyLong