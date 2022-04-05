CREATE TABLE IF NOT EXISTS public.xtcascolumnsecurity
(
    keylong serial NOT NULL,
    tablename character varying(50) COLLATE pg_catalog."default" NOT NULL,
    columnname character varying(50) COLLATE pg_catalog."default" NOT NULL,
    securitytoken character varying(50) COLLATE pg_catalog."default" NOT NULL,
    lastuser character varying(50) COLLATE pg_catalog."default" NOT NULL DEFAULT CURRENT_USER,
    lastdate timestamp without time zone NOT NULL DEFAULT now(),
    lastaction integer NOT NULL DEFAULT 1,
    CONSTRAINT xtcascolumnsecurity_pkey PRIMARY KEY (keylong)
)