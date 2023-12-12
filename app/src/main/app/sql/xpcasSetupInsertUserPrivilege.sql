alter procedure dbo.xpcasSetupInsertUserPrivilege (
	@KeyLong int output,
	@KeyText nvarchar(50) = null,
	@Description nvarchar(50) = null
) as
	if exists (select * from xtcasUserPrivilege
		where KeyText = @KeyText
			and LastAction > 0 or LastAction is null)
    
        update xtCasUserPrivilege set
        	LastAction = 2,
			LastDate = getDate(),
			LastUser = dbo.xfcasUser()
        where KeyText = @KeyText
    
	else
		insert into xtcasUserPrivilege (
			KeyText,
			Description,
			LastAction,
			LastUser,
			LastDate
		) values (
			@KeyText,
			@Description,
			1,
			dbo.xfcasUser(),
			getDate()
		)
    

	if exists (select * from tVersion10
		where KeyText = @KeyText
			and LastAction > 0 or LastAction is null)
    
        update tVersion10 set
        	LastAction = 2,
			LastDate = getDate(),
			LastUser = dbo.xfcasUser()
        where KeyText = @KeyText
    
	else
		insert into tVersion10 (
			KeyText,
			ModuleName
		) values (
			@KeyText,
			'ch.minova.install'
		)
    
	select @KeyLong = @@identity
return @@error