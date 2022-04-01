CREATE TABLE IF NOT EXISTS public.xtcasuserprivilege
(
    keylong serial NOT NULL,
    keytext character varying(50) COLLATE pg_catalog."default" NOT NULL,
    description character varying(50) COLLATE pg_catalog."default",
    lastuser character varying(50) COLLATE pg_catalog."default" NOT NULL DEFAULT CURRENT_USER,
    lastaction integer NOT NULL DEFAULT 1,
    lastdate timestamp without time zone DEFAULT now(),
    transactionchecker character varying(100) COLLATE pg_catalog."default",
    CONSTRAINT xtcasuserprivilege_pkey PRIMARY KEY (keylong)
)