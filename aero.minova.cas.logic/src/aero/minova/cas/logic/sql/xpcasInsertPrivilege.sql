alter procedure dbo.xpcasInsertPrivilege (
	@KeyText nvarchar(50) = null
)
as
	if (not exists(select * from xtcasUserPrivilege
		where KeyText = @KeyText))
	begin
	insert into xtcasUserPrivilege (
		KeyText
	) values (
		@KeyText
	)
	end
