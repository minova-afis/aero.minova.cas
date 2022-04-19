alter view dbo.xvcasCASServices
AS
select
cs.KeyLong as CASServiceKey,
cs.KeyText as ServiceName,
cs.ServiceURL,
cs.Port,
nl.KeyLong as NewsfeedListenerKey,
nl.TableName,
pn.KeyLong as ProcedureNewsfeedKey,
pn.KeyText as ProcedureName
from xtcasCASservices cs
left join xtcasNewsfeedListener nl on nl.CASServiceKey = cs.KeyLong
left join xtcasProcedureNewsfeed pn on pn.TableName = nl. TableName