alter procedure dbo.xpcasInitMdi (
	@ID nvarchar(100),
	@Icon nvarchar(100),
	@Label nvarchar(100),
	@Menu nvarchar(100),
	@Position float,
	@SecurityToken nvarchar(50),
	@MdiTypeKey int,
	@ModulName nvarchar(500)
)
with encryption
as
	declare @Execute bit = 1
	
	if(@MdiTypeKey = 3)
	begin
		if exists(select * from xtcasMdi where LastAction > 0 and MdiTypeKey = 3)
		begin
			set @Execute = 0

			update xtcasMdi set 
				Icon = @Icon,
				Label = @Label,
				Menu = @Menu,
				Position = @Position,
				SecurityToken = @SecurityToken,
				MdiTypeKey = @MdiTypeKey,
				ModulName = @ModulName,
				LastUser = dbo.xfCasUser(),
				LastDate = getDate(),
				LastAction = 2
			where MdiTypeKey = 3 and LastAction > 0
		end
	end 
	
	if (@Execute = 1)
		begin
		if not exists (select * from xtcasMdi where LastAction >-1 and ID = @ID)
		begin
			insert into xtcasMdi(
				ID,
				Icon,
				Label,
				Menu,
				Position,
				SecurityToken,
				MdiTypeKey,
				ModulName,
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
				@ModulName,
				dbo.xfCasUser(),
				getDate(),
				1
			)
		end
		else 
		begin 
			update xtcasMdi set 
					Icon = @Icon,
					Label = @Label,
					Menu = @Menu,
					Position = @Position,
					SecurityToken = @SecurityToken,
					MdiTypeKey = @MdiTypeKey,
					ModulName = @ModulName,
					LastUser = dbo.xfCasUser(),
					LastDate = getDate(),
					LastAction = 2
				where ID = @Id and LastAction > 0
		end 
	end