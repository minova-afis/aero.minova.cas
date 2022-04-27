create view dbo.xvcasCASServiceMessage
with encryption as
select 
    cs.KeyLong as CASServiceKey,
    cs.KeyText as CASServiceName,
    cs.ServiceURL,
    cs.Port,
    sm.KeyLong as MessageKey,
    sm.Message,
    sm.isSent,
    sm.NumberOfAttempts,
    sm.MessageCreationDate
from xtcasCASServices cs
left join xtcasServiceMessage sm on sm.CASServiceKey = cs.KeyLong
where cs.LastAction >=0
  and sm.LastAction >=0
  and sm.isSent = 0