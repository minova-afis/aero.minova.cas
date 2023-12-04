create Table xtxmpExample(
    KeyLong INT NOT NULL IDENTITY,
	Example VARCHAR(20) NOT NULL,
	LastAction int,
	LastDate datetime,
  	LastUser nvarchar(50)
)