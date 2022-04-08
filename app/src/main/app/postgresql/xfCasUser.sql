CREATE OR REPLACE FUNCTION public.xfcasuser(
       )
   RETURNS character varying AS $test$
BEGIN
RETURN current_user;
END;
$test$ LANGUAGE plpgsql;
