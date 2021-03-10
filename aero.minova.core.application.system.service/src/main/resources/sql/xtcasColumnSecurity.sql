CREATE TABLE xtcasColumnSecurity(
	KeyLong INT NOT NULL IDENTITY,
	TableName VARCHAR(50) NOT NULL,
	ColumnName VARCHAR(50) NOT NULL,
	SecurityToken VARCHAR(50) NOT NULL,
)
ALTER TABLE xtcasColumnSecurity
ADD CONSTRAINT PK_xtcasColumnSecurity_KeyLong PRIMARY KEY (KeyLong)