CREATE OR REPLACE VIEW public.xvcasServicePropertiesIndex
as
    select KeyLong,
           KeyText,
           Service,
           Property,
           Val,
           LastAction
    from xtcasServiceProperties
    where LastAction > 0;