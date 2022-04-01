CREATE TABLE IF NOT EXISTS public.xtcasusergroup
(
    keylong integer NOT NULL DEFAULT nextval('xtcasusergroup_keylong_seq'::regclass),
    keytext character varying(50) COLLATE pg_catalog."default" NOT NULL,
    description character varying(50) COLLATE pg_catalog."default",
    usercode character varying(50) COLLATE pg_catalog."default",
    securitytoken character varying(250) COLLATE pg_catalog."default" NOT NULL,
    lastuser character varying(50) COLLATE pg_catalog."default" NOT NULL,
    lastdate timestamp without time zone NOT NULL DEFAULT now(),
    lastaction integer DEFAULT 1,
    CONSTRAINT xtcasusergroup_pkey PRIMARY KEY (keylong),
    CONSTRAINT xtcasusergroup_keytext_key UNIQUE (keytext)
)