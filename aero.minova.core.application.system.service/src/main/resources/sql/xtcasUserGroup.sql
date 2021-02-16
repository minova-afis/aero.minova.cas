CREATE TABLE xtcasUserGroup (
    KeyLong INT NOT NULL IDENTITY,
    KeyText VARCHAR(50) NOT NULL,
	Description VARCHAR(50),
	UserCode VARCHAR(50),
    SecurityToken VARCHAR(250) NOT NULL
)

ALTER TABLE xtcasUserGroup
ADD CONSTRAINT UQ_xtcasUserGroup UNIQUE (KeyLong,KeyText),
    CONSTRAINT PK_xtcasUserGroup_KeyLong PRIMARY KEY (KeyLong)
