CREATE TABLE tUserRoles (
	UserKey INT NOT NULL,
	UserGroupKey INT NOT NULL,
    LastUser VARCHAR(50),
    LastDate DATETIME NOT NULL,
    LastAction INT NOT NULL
)

ALTER TABLE tUserRoles
ADD CONSTRAINT FK_tUserRoles_GroupKey FOREIGN KEY (UserGroupKey) REFERENCES tUserGroup (KeyLong),
	CONSTRAINT FK_tUserRoles_Key FOREIGN KEY (UserKey) REFERENCES tUser (KeyLong),
 	CONSTRAINT DF_tUserRoles_LastUser DEFAULT 'dbo' FOR LastUser,
 	CONSTRAINT DF_tUserRoles_LastDate DEFAULT GETDATE() FOR LastDate,
 	CONSTRAINT DF_tUserRoles_LastAction DEFAULT 1 FOR LastAction