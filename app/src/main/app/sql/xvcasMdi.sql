alter view dbo.xvcasMdi
with encryption as
	select id,
			icon,
			label,
			menu,
			position,
			securitytoken,
			mdiTypeKey
	from xtcasMdi 
	where LastAction > 0
	order by position asc