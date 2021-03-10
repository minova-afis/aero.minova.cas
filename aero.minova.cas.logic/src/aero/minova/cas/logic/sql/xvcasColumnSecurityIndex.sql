alter view dbo.xvcasColumnSecurityIndex
with encryption as
	select	cs.KeyLong,
			cs.TableName,
			cs.ColumnName,
			cs.SecurityToken
	from xtcasColumnSecurity cs
	where cs.LastAction > 0