ALTER TABLE xtcasAuthorities ADD FOREIGN KEY (Authority) REFERENCES xtcasUserGroup(KeyText)
ALTER TABLE xtcasAuthorities ADD FOREIGN KEY (Username) REFERENCES xtcasUsers(Username)