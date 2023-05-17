alter view dbo.xvcasCASServiceMessage
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
    sm.MessageCreationDate,
    sm.Failed,
    cs.ServiceMessageReceiverLoginTypeKey,
    cs.Username,
    cs.Password,
    cs.ClientID,
    cs.ClientSecret,
    cs.TokenURL
from xtcasCASServices cs
left join xtcasServiceMessage sm on sm.CASServiceKey = cs.KeyLong
where cs.LastAction >0
  and sm.LastAction >0
  and sm.isSent = 0