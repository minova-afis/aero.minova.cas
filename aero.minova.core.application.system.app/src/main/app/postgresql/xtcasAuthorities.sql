CREATE TABLE IF NOT EXISTS public.xtcasauthorities
(
    keylong integer NOT NULL DEFAULT nextval('xtcasauthorities_keylong_seq'::regclass),
    username character varying(50) COLLATE pg_catalog."default" NOT NULL,
    authority character varying(50) COLLATE pg_catalog."default" NOT NULL,
    lastuser character varying(50) COLLATE pg_catalog."default",
    lastdate timestamp without time zone DEFAULT now(),
    lastaction integer DEFAULT 1,
    CONSTRAINT xtcasauthorities_pkey PRIMARY KEY (keylong),
    CONSTRAINT xtcasauthorities_username_authority_key UNIQUE (username, authority),
    CONSTRAINT xtcasauthorities_authority_fkey FOREIGN KEY (authority)
        REFERENCES public.xtcasusergroup (keytext) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT xtcasauthorities_username_fkey FOREIGN KEY (username)
        REFERENCES public.xtcasusers (username) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)