CREATE TABLE tUser (
    KeyLong INT NOT NULL IDENTITY,
    KeyText VARCHAR(50) NOT NULL,
    UserSecurityToken VARCHAR(50) NOT NULL,
	Memberships VARCHAR(250)
)

ALTER TABLE tUser
ADD CONSTRAINT UQ_tUser UNIQUE (KeyLong,UserSecurityToken,KeyText),
    CONSTRAINT PK_tUser_KeyLong PRIMARY KEY (KeyLong)
