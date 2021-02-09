CREATE TABLE xtcasUser (
    KeyLong INT NOT NULL IDENTITY,
    KeyText VARCHAR(50) NOT NULL,
    UserSecurityToken VARCHAR(50) NOT NULL,
	Memberships VARCHAR(250)
)

ALTER TABLE xtcasUser
ADD CONSTRAINT UQ_xtcasUser UNIQUE (KeyLong,UserSecurityToken,KeyText),
    CONSTRAINT PK_xtcasUser_KeyLong PRIMARY KEY (KeyLong)
