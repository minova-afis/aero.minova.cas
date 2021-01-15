CREATE TABLE tUser (
    KeyLong INT NOT NULL IDENTITY,
    KeyText VARCHAR(50) NOT NULL,
    Description VARCHAR (50),
    UserSecurityToken VARCHAR(50) NOT NULL,
	Active TINYINT NOT NULL DEFAULT 1,
    LastUser VARCHAR(50),
    LastDate DATETIME NOT NULL,
    LastAction INT NOT NULL
)

ALTER TABLE tUser
ADD CONSTRAINT UQ_tUser UNIQUE (KeyLong,UserSecurityToken,KeyText),
    CONSTRAINT PK_tUser_KeyLong PRIMARY KEY (KeyLong),
 	CONSTRAINT DF_tUser_LastUser DEFAULT 'dbo' FOR LastUser,
 	CONSTRAINT DF_tUser_LastDate DEFAULT GETDATE() FOR LastDate,
 	CONSTRAINT DF_tUser_LastAction DEFAULT 1 FOR LastAction;
