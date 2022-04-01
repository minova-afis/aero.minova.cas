CREATE TABLE IF NOT EXISTS public.xtcascolumnsecurity
(
    keylong integer NOT NULL DEFAULT nextval('xtcascolumnsecurity_keylong_seq'::regclass),
    tablename character varying(50) COLLATE pg_catalog."default" NOT NULL,
    columnname character varying(50) COLLATE pg_catalog."default" NOT NULL,
    securitytoken character varying(50) COLLATE pg_catalog."default" NOT NULL,
    lastuser character varying(50) COLLATE pg_catalog."default" NOT NULL,
    lastdate timestamp without time zone NOT NULL DEFAULT now(),
    lastaction integer NOT NULL DEFAULT 1,
    CONSTRAINT xtcascolumnsecurity_pkey PRIMARY KEY (keylong)
)