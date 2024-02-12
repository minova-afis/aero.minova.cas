alter procedure dbo.xpcasSetupInsertUserPrivilege (
	@KeyLong int output,
	@KeyText nvarchar(50) = null,
	@Description nvarchar(50) = null
) as
	if exists (select * from xtcasUserPrivilege
		where KeyText = @KeyText
		  and LastAction < 0
		  or LastAction is null)
    begin
        update xtCasUserPrivilege
        set LastAction = 1,
		LastDate = getDate(),
		LastUser = dbo.xfcasUser
        where KeyText = @KeyText
    end
	else
	begin
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
    end

	if exists (select * from tVersion10
		where KeyText = @KeyText
		  and LastAction < 0 or LastAction is null)
    begin
        update tVersion10
        set LastAction = 1,
		LastDate = getDate(),
		LastUser = dbo.xfcasUser()
        where KeyText = @KeyText
    end
	else
	begin
		insert into tVersion10 (
			KeyText,
			ModuleName
		) values (
			@KeyText,
			'ch.minova.install'
		)
	end
    
	select @KeyLong = @@identity
return @@error