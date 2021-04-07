CREATE TABLE xtcasUsers (
	KeyLong int identity not null,
  	Username VARCHAR(50) NOT NULL,
	Password VARCHAR(100) NOT NULL,
	LastAction int,
	LastDate datetime,
  	LastUser nvarchar(50),
);

ALTER TABLE xtcasUsers ADD CONSTRAINT DF_xtcasUsers_LastUser DEFAULT (user_name()) FOR LastUser,
ALTER TABLE xtcasUsers ADD CONSTRAINT DF_xtcasUsers_LastDate DEFAULT (getdate()) FOR LastDate,
ALTER TABLE xtcasUsers ADD CONSTRAINT DF_xtcasUsers_LastAction DEFAULT ((1)) FOR LastAction,
ALTER TABLE xtcasUsers ADD CONSTRAINT QF_xtcasUsers_Username UNIQUE (Username)
  
