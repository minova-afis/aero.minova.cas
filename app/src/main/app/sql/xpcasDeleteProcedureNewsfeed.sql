create procedure dbo.xpcasDeleteProcedureNewsfeed (
    @KeyLong int
)
as

update xtcasProcedureNewsfeed
set 
LastAction = -1,
LastUser = dbo.xfCasUser(),
LastDate = getDate()
where 
KeyLong = @KeyLong