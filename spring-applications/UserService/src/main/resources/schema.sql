
DROP TABLE IF EXISTS USERS;

CREATE TABLE USERS
(
    id        uuid default random_uuid() PRIMARY KEY,
    firstname VARCHAR NOT NULL,
    lastname  VARCHAR NOT NULL,
    ssn       VARCHAR NOT NULL
);
INSERT INTO USERS(id, firstname, lastname, ssn)
VALUES ('531ffa4b-f380-4df2-b477-a794be8ef49c', 'John', 'Doe', '123456789');