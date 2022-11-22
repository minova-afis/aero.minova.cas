alter procedure dbo.xpcasInitMdi (
	@ID nvarchar(100),
	@Icon nvarchar(100),
	@Label nvarchar(100),
	@Menu nvarchar(100),
	@Position float,
	@SecurityToken nvarchar(50),
	@MdiTypeKey int
)
with encryption
as
	declare @Execute bit = 1
	
	if(@MdiTypeKey = 3)
	begin
		if exists(select * from xtcasMdi where LastAction > 0 and MdiTypeKey = 3)
		begin
			set @Execute = 0
		end
	end 
	
	if not exists (select * from xtcasMdi where LastAction >-1 and ID = @ID)
	begin
		if (@Execute = 1)
		begin
			insert into xtcasMdi(
				ID,
				Icon,
				Label,
				Menu,
				Position,
				SecurityToken,
				MdiTypeKey,
				LastUser,
				LastDate,
				LastAction
			) values (
				@ID,
				@Icon,
				@Label,
				@Menu,
				@Position,
				@SecurityToken,
				@MdiTypeKey,
				dbo.xfCasUser(),
				getDate(),
				1
			)
		end
	end
