alter view dbo.xvcasMdi
with encryption as
	select keytext,
			icon,
			label,
			menu,
			position,
			securitytoken,
			mdiTypeKey
	from xtcasMdi 
	where LastAction > 0
