CREATE OR REPLACE FUNCTION public.xfcasuser(
	)
    RETURNS character varying
    LANGUAGE 'sql'
    COST 100
    VOLATILE PARALLEL UNSAFE

RETURN current_setting('my.app_user'::text);