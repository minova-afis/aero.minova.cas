CREATE TABLE IF NOT EXISTS public.xtcasuser
(
    keylong integer NOT NULL DEFAULT nextval('xtcasuser_keylong_seq'::regclass),
    keytext character varying(10) COLLATE pg_catalog."default" NOT NULL,
    usersecuritytoken character varying(50) COLLATE pg_catalog."default" NOT NULL,
    memberships character varying(250) COLLATE pg_catalog."default",
    lastuser character varying(50) COLLATE pg_catalog."default" NOT NULL DEFAULT CURRENT_USER,
    lastdate timestamp without time zone NOT NULL DEFAULT now(),
    lastaction integer DEFAULT 1,
    CONSTRAINT xtcasuser_pkey PRIMARY KEY (keylong)
)