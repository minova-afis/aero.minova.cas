alter view dbo.xvcasCASServices
AS
select * from xtcasCASservices cs
left join xtcasNewsfeedListener nl on nl.CASServiceKey = cs.KeyLong
left join xtcasProcedureNewsfeed pn on pn.TableName = nl. TableName