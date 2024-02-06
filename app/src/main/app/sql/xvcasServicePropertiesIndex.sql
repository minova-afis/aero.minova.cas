alter view dbo.xvcasServicePropertiesIndex
with encryption as
    select KeyLong,
           KeyText,
           Service,
           Property,
           Val,
           LastAction
    from xtcasServiceProperties
    where LastAction > 0