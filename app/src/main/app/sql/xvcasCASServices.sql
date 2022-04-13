alter view dbo.xvcasCASServices
AS
select
cs.KeyText as ServiceName,
cs.ServiceURL.
cs.Port,
nl.TableName,
pn.KeyText as ProcedureName
from xtcasCASservices cs
left join xtcasNewsfeedListener nl on nl.CASServiceKey = cs.KeyLong
left join xtcasProcedureNewsfeed pn on pn.TableName = nl. TableName