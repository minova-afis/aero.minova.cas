alter procedure dbo.xpcasUpdateColumnSecurity (
	@KeyLong int,
	@TableName nvarchar(50) = null,
	@ColumnName nvarchar(50) = null,
	@SecurityToken nvarchar(50) = null,
	@KeyText nvarchar(50)
)
with encryption as
	if (exists(select * from xtcasColumnSecurity
		where TableName = @TableName
		  and ColumnName = @ColumnName
		  and KeyLong <> @KeyLong
		  and LastAction > 0))
	begin
		raiserror('ADO | 25 | msg.sql.asdfTODO | Die Kombination Tabelle / Spalte darf nicht doppelt angelegt werden!', 16, 1) with seterror
		return -1
	end

	update xtcasColumnSecurity
	set TableName = @TableName,
		ColumnName = @ColumnName,
		SecurityToken = @SecurityToken,
		KeyText = @KeyText,
		LastUser = dbo.xfCasUser(),
		LastDate = getDate(),
		LastAction = 2
	where KeyLong = @KeyLong
return @@error