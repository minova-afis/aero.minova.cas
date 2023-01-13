CREATE TABLE IF NOT EXISTS public.xtcaserror
(
    keylong serial NOT NULL,
    username character varying(10) COLLATE pg_catalog."default" NOT NULL,
    errormessage character varying(250) COLLATE pg_catalog."default" NOT NULL,
    date timestamp without time zone NOT NULL DEFAULT now(),
    CONSTRAINT xtcaserror_pkey PRIMARY KEY (keylong)
);