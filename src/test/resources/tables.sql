CREATE TABLE users (
  UserID int NOT NULL AUTO_INCREMENT,
  Email VARCHAR(50) NOT NULL,
  Password VARCHAR(50) NOT NULL,
  RealName VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY (UserID),
  UNIQUE KEY `idx_Email` (`Email`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

# Email column in this table is needed for security realm in context.xml
CREATE TABLE userrole (
  UserID int NOT NULL,
  RoleName VARCHAR(50) NOT NULL,
  Email VARCHAR(50) NOT NULL,
  PRIMARY KEY (UserID, RoleName),
  CONSTRAINT `e_FK_urole` FOREIGN KEY (UserId) REFERENCES users (UserId) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

# mediumblob - will store files up to 16MB
CREATE TABLE files (
  FileID int NOT NULL AUTO_INCREMENT,
  UserID int NOT NULL,
  FileName VARCHAR(50) NOT NULL,
  FileSize int NOT NULL,
  UploadDate date NOT NULL DEFAULT '0000-00-00',
  File mediumblob,
  PRIMARY KEY (FileID),
  CONSTRAINT `e_FK_file` FOREIGN KEY (UserId) REFERENCES users (UserId) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


INSERT INTO users (Email, Password, RealName) VALUES ('sln@dot.com', 'aaa', 'Sln');
INSERT INTO userrole VALUES (LAST_INSERT_ID(), 'admin', 'sln@dot.com');
