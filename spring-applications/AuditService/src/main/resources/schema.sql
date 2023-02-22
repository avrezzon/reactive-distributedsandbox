DROP TABLE IF EXISTS TRANSACTIONS;

CREATE TABLE TRANSACTIONS(

    transaction_uuid UUID default random_uuid() NOT NULL PRIMARY KEY,
    acting_user int NOT NULL,
    operation VARCHAR NOT NULL,
    status VARCHAR NOT NULL,
    account int NOT NULL,
    amount NUMBER NOT NULL,
    to_account int,
    event_timestamp TIMESTAMP default current_timestamp() NOT NULL
);

INSERT INTO TRANSACTIONS(acting_user, operation, status, account, amount, to_account)
VALUES ( 1, 'DEPOSIT', 'SUCCESS', '3213212', 200.36, 0);