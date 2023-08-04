alter procedure dbo.xpcasReadColumnSecurity (
	@KeyLong int output,
	@TableName nvarchar(50) = null output,
	@ColumnName nvarchar(50) = null output,
	@SecurityToken nvarchar(50) = null output,
	@KeyText nvarchar(50) = null output
)
with encryption as
	select	@TableName = TableName,
			@ColumnName = ColumnName,
			@SecurityToken = SecurityToken,
			@KeyText = KeyText
	from xtcasColumnSecurity
	where KeyLong = @KeyLong
return @@error