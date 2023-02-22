DROP TABLE IF EXISTS ACCOUNT;

CREATE TABLE ACCOUNT
(
    id           int default secure_rand(4)  PRIMARY KEY,
    owner_id     UUID  NOT NULL,
    account_type VARCHAR NOT NULL,
    balance      NUMBER  NOT NULL
);




INSERT INTO ACCOUNT(owner_id, account_type, balance)
VALUES ('531ffa4b-f380-4df2-b477-a794be8ef49c','CHECKING', 100.20);
