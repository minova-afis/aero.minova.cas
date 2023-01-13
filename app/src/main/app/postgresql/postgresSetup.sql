CREATE TABLE IF NOT EXISTS public.xtcasuser
(
    keylong serial NOT NULL,
    keytext character varying(10) COLLATE pg_catalog."default" NOT NULL,
    usersecuritytoken character varying(50) COLLATE pg_catalog."default" NOT NULL,
    memberships character varying(250) COLLATE pg_catalog."default",
    lastuser character varying(50) COLLATE pg_catalog."default" NOT NULL DEFAULT CURRENT_USER,
    lastdate timestamp without time zone NOT NULL DEFAULT now(),
    lastaction integer DEFAULT 1,
    CONSTRAINT xtcasuser_pkey PRIMARY KEY (keylong)
);

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
);

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

CREATE TABLE IF NOT EXISTS public.xtcaserror
(
    keylong serial NOT NULL,
    username character varying(10) COLLATE pg_catalog."default" NOT NULL,
    errormessage character varying(250) COLLATE pg_catalog."default" NOT NULL,
    date timestamp without time zone NOT NULL DEFAULT now(),
    CONSTRAINT xtcaserror_pkey PRIMARY KEY (keylong)
);

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
);

CREATE TABLE IF NOT EXISTS public.xtcasauthorities
(
    keylong serial NOT NULL,
    username character varying(50) COLLATE pg_catalog."default" NOT NULL,
    authority character varying(50) COLLATE pg_catalog."default" NOT NULL,
    lastuser character varying(50) COLLATE pg_catalog."default" DEFAULT CURRENT_USER,
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
);

CREATE TABLE IF NOT EXISTS public.xtcasluuserprivilegeusergroup
(
    keylong serial NOT NULL,
    userprivilegekey integer NOT NULL,
    usergroupkey integer NOT NULL,
    rowlevelsecurity integer NOT NULL DEFAULT 0,
    lastuser character varying(50) COLLATE pg_catalog."default" NOT NULL DEFAULT CURRENT_USER,
    lastdate timestamp without time zone NOT NULL DEFAULT now(),
    lastaction integer NOT NULL DEFAULT 1,
    CONSTRAINT xtcasluuserprivilegeusergroup_pkey PRIMARY KEY (keylong),
    CONSTRAINT xtcasluuserprivilegeusergroup_usergroupkey_fkey FOREIGN KEY (usergroupkey)
        REFERENCES public.xtcasusergroup (keylong) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT xtcasluuserprivilegeusergroup_userprivilegekey_fkey FOREIGN KEY (userprivilegekey)
        REFERENCES public.xtcasuserprivilege (keylong) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE OR REPLACE VIEW public.xvcasuserprivileges
 AS
 SELECT up.keylong,
    up.keytext AS privilegekeytext,
    ug.keytext,
    upg.rowlevelsecurity
   FROM xtcasuserprivilege up
     JOIN xtcasluuserprivilegeusergroup upg ON up.keylong = upg.userprivilegekey AND upg.lastaction > 0
     JOIN xtcasusergroup ug ON ug.keylong = upg.usergroupkey AND ug.lastaction > 0
  WHERE up.lastaction > 0;


CREATE OR REPLACE FUNCTION public.xfcasuser(
       )
   RETURNS character varying AS $test$
BEGIN
RETURN current_user;
END;
$test$ LANGUAGE plpgsql;

CREATE OR REPLACE VIEW public.xvcasUserSecurity
AS
SELECT up.KeyLong,
			up.KeyText AS PrivilegeKeyText,
			ug.SecurityToken,
			upg.RowLevelSecurity
	FROM xtcasUserPrivilege up
	INNER JOIN xtcasLuUserPrivilegeUserGroup upg ON up.KeyLong = upg.UserPrivilegeKey AND upg.LastAction > 0
	INNER JOIN xtcasUserGroup ug ON ug.KeyLong = upg.UserGroupKey AND ug.LastAction > 0
	WHERE up.LastAction > 0;