CREATE TABLE IF NOT EXISTS public.xtcasusergroup
(
    keylong serial NOT NULL ,
    keytext character varying(50) COLLATE pg_catalog."default" NOT NULL,
    description character varying(50) COLLATE pg_catalog."default",
    usercode character varying(50) COLLATE pg_catalog."default",
    securitytoken character varying(250) COLLATE pg_catalog."default" NOT NULL,
    lastuser character varying(50) COLLATE pg_catalog."default" NOT NULL DEFAULT CURRENT_USER,
    lastdate timestamp without time zone NOT NULL DEFAULT now(),
    lastaction integer DEFAULT 1,
    CONSTRAINT xtcasusergroup_pkey PRIMARY KEY (keylong),
    CONSTRAINT xtcasusergroup_keytext_key UNIQUE (keytext)
);