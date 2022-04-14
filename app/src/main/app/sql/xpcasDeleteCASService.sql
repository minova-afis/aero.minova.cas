create procedure dbo.xpcasDeleteCASService (
    @KeyLong int
)
as

update xtcasCASServices
set 
LastAction = -1,
LastUser = dbo.xfCasUser(),
LastDate = getDate()
where 
KeyLong = @KeyLong