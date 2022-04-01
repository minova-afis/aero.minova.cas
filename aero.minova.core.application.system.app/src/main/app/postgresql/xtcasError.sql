CREATE TABLE IF NOT EXISTS public.xtcaserror
(
    keylong integer NOT NULL DEFAULT nextval('xtcaserror_keylong_seq'::regclass),
    username character varying(10) COLLATE pg_catalog."default" NOT NULL,
    errormessage character varying(250) COLLATE pg_catalog."default" NOT NULL,
    date timestamp without time zone NOT NULL DEFAULT now(),
    CONSTRAINT xtcaserror_pkey PRIMARY KEY (keylong)
)