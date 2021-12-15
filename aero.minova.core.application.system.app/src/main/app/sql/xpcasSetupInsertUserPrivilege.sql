alter procedure dbo.xpcasSetupInsertUserPrivilege (
	@KeyLong int output,
	@KeyText nvarchar(50) = null,
	@Description nvarchar(50) = null
) as
	if (exists (select * from xtcasUserPrivilege
		where KeyText = @KeyText
		  and LastAction < 0))
    begin
        update xtCasUserPrivilege
        set LastAction = 1
        where KeyText = @KeyText
    end
	else if (not exists(select * from xtcasUserPrivilege
		where KeyText = @KeyText
		  and LastAction > 0))
	begin
		insert into xtcasUserPrivilege (
		KeyText,
		Description
	) values (
		@KeyText,
		@Description
	)
    end

	select @KeyLong = @@identity
return @@error