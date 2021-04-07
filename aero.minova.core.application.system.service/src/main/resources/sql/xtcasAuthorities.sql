CREATE TABLE xtcasAuthorities (
	KeyLong int identity not null,
 	Username VARCHAR(50) NOT NULL,
	Authority VARCHAR(50) NOT NULL,
	LastAction int,
	LastDate datetime,
	LastUser nvarchar(50),
  FOREIGN KEY (Username) REFERENCES xtcasUsers(Username)
);

ALTER TABLE xtcasAuthorities ADD CONSTRAINT DF_xtcasAuthorities_LastUser DEFAULT (user_name()) FOR LastUser,
ALTER TABLE xtcasAuthorities ADD CONSTRAINT DF_xtcasAuthorities_LastDate DEFAULT (getdate()) FOR LastDate,
ALTER TABLE xtcasAuthorities ADD CONSTRAINT DF_xtcasAuthorities_LastAction DEFAULT (1) FOR LastAction

CREATE UNIQUE INDEX ix_auth_username
  on xtcasAuthorities (Username,Authority);