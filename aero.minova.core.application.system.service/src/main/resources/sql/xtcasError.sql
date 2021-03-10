create table xtcasError(
	KeyLong INT NOT NULL IDENTITY,
	Username VARCHAR(10) NOT NULL,
	ErrorMessage VARCHAR(250) NOT NULL,
	Date DATETIME NOT NULL,
)
ALTER TABLE xtcasError
ADD CONSTRAINT PK_xtcasError_KeyLong PRIMARY KEY (KeyLong)