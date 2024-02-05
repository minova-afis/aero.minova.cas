alter view dbo.xvcasMdiIndex
with encryption as
	select	m.KeyLong,
			m.KeyText,
			m.Icon,
			m.Label,
			m.Menu,
			m.Position,
			m.SecurityToken,
			mt.KeyText as MdiTypeKeyText,
			m.ModulName,
			m.LastUser,
			m.LastDate,
			m.LastAction
	from xtcasMdi m
	left join xtcasMdiType mt on m.MdiTypeKey = mt.KeyLong
	where m.LastAction > 0