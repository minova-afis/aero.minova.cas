alter procedure dbo.xpcasInitMdi (
	@KeyText nvarchar(50),
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
	-- Änderungen hier sollten auch in der Methode MdiService#saveMdi durchgeführt werden!
	declare @Execute bit = 1
	
	if(@MdiTypeKey = 3)
	begin
		if exists(select * from xtcasMdi where LastAction > 0 and MdiTypeKey = 3)
		begin
			set @Execute = 0
		end
	end 
	
	if (@Execute = 1)
		begin
		if not exists (select * from xtcasMdi where LastAction > 0 and KeyText = @KeyText)
		begin

			if (@MdiTypeKey <> 1)
			begin
				set @SecurityToken = null 
			end

			insert into xtcasMdi(
				KeyText,
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
				@KeyText,
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
	end