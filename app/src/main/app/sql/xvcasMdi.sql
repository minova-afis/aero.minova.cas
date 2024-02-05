alter view dbo.xvcasMdi
with encryption as
	select keytext,
			icon,
			label,
			menu,
			position,
			securitytoken,
			mdiTypeKey,
			LastAction
	from xtcasMdi 
	where LastAction > 0
