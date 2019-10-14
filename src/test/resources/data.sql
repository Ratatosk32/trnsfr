DROP TABLE IF EXISTS Account;

CREATE TABLE Account (AccountId LONG PRIMARY KEY AUTO_INCREMENT NOT NULL, UserName VARCHAR(30), Balance DECIMAL(19,4));

INSERT INTO Account (UserName,Balance) VALUES ('test', 50.0000);
INSERT INTO Account (UserName,Balance) VALUES ('test1', 150.0000);
INSERT INTO Account (UserName,Balance) VALUES ('test', 200.0000);
INSERT INTO Account (UserName,Balance) VALUES ('test2', 300.0000);
INSERT INTO Account (UserName,Balance) VALUES ('test1', 500.0000);
