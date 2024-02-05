alter view dbo.xvcasColumnSecurityIndex
with encryption as
	select	cs.KeyLong,
			cs.KeyText,
			cs.TableName,
			cs.ColumnName,
			cs.SecurityToken,
			cs.LastAction
	from xtcasColumnSecurity cs
	where cs.LastAction > 0