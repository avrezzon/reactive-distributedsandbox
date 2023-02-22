
DROP TABLE IF EXISTS ERROR_HISTORY;

CREATE TABLE ERROR_HISTORY
(
    id        uuid default random_uuid() PRIMARY KEY,
    app_name  VARCHAR NOT NULL ,
    endpoint  VARCHAR NOT NULL ,
    payload   JSON  NOT NULL ,
    message   VARCHAR NOT NULL ,
    status_code INT NOT NULL ,
    transaction_id VARCHAR NOT NULL,
    event_timestamp TIMESTAMP default current_timestamp() NOT NULL
);
