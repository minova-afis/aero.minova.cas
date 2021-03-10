create table xtcasLuUserPrivilegeUserGroup (
    KeyLong INT NOT NULL IDENTITY,
    UserPrivilegeKey INT NOT NULL,
    UserGroupKey INT NOT NULL,
	RowLevelSecurity BIT
)

ALTER TABLE xtcasLuUserPrivilegeUserGroup
ADD CONSTRAINT PK_xtcasLuUserPrivilegesUserGroup_KeyLong PRIMARY KEY (KeyLong),
	CONSTRAINT FK_xtcasLuUserPrivilegesUserGroup_UserGroupKey Foreign KEY (UserGroupKey) REFERENCES xtcasUserGroup (KeyLong),
	CONSTRAINT FK_xtcasLuUserPrivilegesUserGroup_UserPrivilegeKey FOREIGN KEY (UserPrivilegeKey) REFERENCES xtcasUserPrivilege (KeyLong),
	CONSTRAINT DF_xtcasLuUserPrivilegesUserGroup_RowLevelSecurity DEFAULT 0 FOR RowLevelSecurity;
