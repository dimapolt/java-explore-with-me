--DROP TABLE IF EXISTS statistic;

CREATE TABLE IF NOT EXISTS statistic (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    app        VARCHAR(50)                     NOT NULL,
    uri        VARCHAR(250)                    NOT NULL,
    ip         VARCHAR(45)                     NOT NULL,
    time_stamp TIMESTAMP WITHOUT TIME ZONE     NOT NULL,

    CONSTRAINT statistic_p_key PRIMARY KEY (id)
);