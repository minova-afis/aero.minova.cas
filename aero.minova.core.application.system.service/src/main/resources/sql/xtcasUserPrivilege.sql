CREATE TABLE xtcasUserPrivilege (
    KeyLong INT NOT NULL IDENTITY,
    KeyText VARCHAR(50) NOT NULL,
)

ALTER TABLE xtcasUserPrivilege
ADD CONSTRAINT UQ_xtcasUserPrivilege UNIQUE (KeyText),
    CONSTRAINT PK_xtcasUserPrivilege_KeyLong PRIMARY KEY (KeyLong)
