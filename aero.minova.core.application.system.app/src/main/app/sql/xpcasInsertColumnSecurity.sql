alter procedure dbo.xpcasInsertColumnSecurity (
	@KeyLong int output,
	@TableName nvarchar(50) = null,
	@ColumnName nvarchar(50) = null,
	@SecurityToken nvarchar(50) = null
)
with encryption as
	if (exists(select * from xtcasColumnSecurity
		where TableName = @TableName
		  and ColumnName = @ColumnName
		  and LastAction > 0))
	begin
		raiserror('ADO | 25 | msg.sql.asdfTODO | Die Kombination Tabelle / Spalte darf nicht doppelt angelegt werden!', 16, 1) with seterror
		return -1
	end

	insert into xtcasColumnSecurity (
		TableName,
		ColumnName,
		SecurityToken
	) values (
		@TableName,
		@ColumnName,
		@SecurityToken
	)

	select @KeyLong = @@identity
return @@error