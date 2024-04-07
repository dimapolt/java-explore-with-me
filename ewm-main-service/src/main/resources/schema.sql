--DROP TABLE IF EXISTS users, locations, categories, events, requests, compilations, compilations_events;

CREATE TABLE IF NOT EXISTS users (
    id    BIGINT      GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name  VARCHAR(255)                                 NOT NULL,
    email VARCHAR(512)                                 NOT NULL,

    CONSTRAINT users_p_key    PRIMARY KEY (id),
    CONSTRAINT users_email_uq UNIQUE      (email)
);

CREATE TABLE IF NOT EXISTS locations (
    id  BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    lat FLOAT                                   NOT NULL,
    lon FLOAT                                   NOT NULL,

    CONSTRAINT locations_p_key PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS categories (
    id   BIGINT      GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(50)                                  NOT NULL,

    CONSTRAINT categories_p_key   PRIMARY KEY (id),
    CONSTRAINT categories_name_uq UNIQUE      (name)
);

CREATE TABLE IF NOT EXISTS events (
    id                  BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    annotation          VARCHAR(2000)                           NOT NULL,
    category            BIGINT                                  NOT NULL,
    confirmed_requests  INTEGER                                 NOT NULL,
    created_on          TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    description         VARCHAR(7000)                           NOT NULL,
    event_date          TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    initiator           BIGINT                                  NOT NULL,
    location            BIGINT                                  NOT NULL,
    paid                BOOLEAN                                 NOT NULL,
    participant_limit   INTEGER                                 NOT NULL,
    published_on        TIMESTAMP WITHOUT TIME ZONE,
    request_moderation  BOOLEAN                                 NOT NULL,
    state               VARCHAR(20)                             NOT NULL,
    title               VARCHAR(120)                            NOT NULL,
    views               INTEGER                                 NOT NULL,

    CONSTRAINT events_p_key        PRIMARY KEY (id),
    CONSTRAINT events_category_fk  FOREIGN KEY (category)   REFERENCES categories (id) ON DELETE CASCADE,
    CONSTRAINT events_location_fk  FOREIGN KEY (location)   REFERENCES locations (id)  ON DELETE CASCADE,
    CONSTRAINT events_initiator_fk FOREIGN KEY (initiator)  REFERENCES users (id)      ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS requests (
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    created    TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    event      BIGINT                                  NOT NULL,
    requester  BIGINT                                  NOT NULL,
    status     VARCHAR(20)                             NOT NULL,

    CONSTRAINT requests_p_key        PRIMARY KEY (id),
    CONSTRAINT requests_event_fk     FOREIGN KEY (event)     REFERENCES events (id) ON DELETE CASCADE,
    CONSTRAINT requests_requester_fk FOREIGN KEY (requester) REFERENCES users (id)  ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS compilations (
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    pinned     BOOLEAN                                 NOT NULL,
    title      VARCHAR(50)                             NOT NULL,

    CONSTRAINT compilations_p_key PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS compilations_events (
    compilation BIGINT  NOT NULL,
    event       BIGINT  NOT NULL,

    CONSTRAINT compilations_events_compilation_fk FOREIGN KEY (compilation) REFERENCES compilations (id) ON DELETE CASCADE,
    CONSTRAINT compilations_events_event_fk       FOREIGN KEY (event)       REFERENCES events (id)       ON DELETE CASCADE
);