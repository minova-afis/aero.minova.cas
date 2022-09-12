alter view dbo.xvcasServicePropertiesIndex
with encryption as
	select	KeyLong, Service, Property, Val
	from xtcasServiceProperties
	where LastAction > 0