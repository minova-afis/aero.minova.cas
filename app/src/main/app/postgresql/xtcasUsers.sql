CREATE TABLE IF NOT EXISTS public.xtcasusers
(
    keylong serial NOT NULL,
    username character varying(50) COLLATE pg_catalog."default" NOT NULL,
    password character varying(150) COLLATE pg_catalog."default" NOT NULL,
    lastaction integer NOT NULL DEFAULT 1,
    lastdate timestamp without time zone NOT NULL DEFAULT now(),
    CONSTRAINT "xtcasUsers_pkey" PRIMARY KEY (keylong),
    CONSTRAINT xtcasusers_username_key UNIQUE (username)
);
