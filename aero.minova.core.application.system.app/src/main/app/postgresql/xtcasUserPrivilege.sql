CREATE TABLE IF NOT EXISTS public.xtcasuserprivilege
(
    keylong integer NOT NULL DEFAULT nextval('xtcasuserprivilege_keylong_seq'::regclass),
    keytext character varying(50) COLLATE pg_catalog."default" NOT NULL,
    description character varying(50) COLLATE pg_catalog."default",
    lastuser character varying(50) COLLATE pg_catalog."default" NOT NULL,
    lastaction integer NOT NULL DEFAULT 1,
    lastdate timestamp without time zone DEFAULT now(),
    transactionchecker character varying(100) COLLATE pg_catalog."default",
    CONSTRAINT xtcasuserprivilege_pkey PRIMARY KEY (keylong)
)