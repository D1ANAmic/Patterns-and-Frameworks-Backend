CREATE TABLE players
(
    id       BIGINT NOT NULL,
    name     VARCHAR(255),
    email    VARCHAR(255),
    password VARCHAR(255),
    CONSTRAINT pk_players PRIMARY KEY (id)
);

CREATE TABLE teams
(
    id              BIGINT NOT NULL,
    name            VARCHAR(255),
    level           INTEGER,
    score           INTEGER,
    lives           INTEGER,
    active          BOOLEAN,
    player_left_id  BIGINT,
    player_right_id BIGINT,
    CONSTRAINT pk_teams PRIMARY KEY (id)
);

ALTER TABLE teams
    ADD CONSTRAINT FK_TEAMS_ON_PLAYER_LEFT FOREIGN KEY (player_left_id) REFERENCES players (id);

ALTER TABLE teams
    ADD CONSTRAINT FK_TEAMS_ON_PLAYER_RIGHT FOREIGN KEY (player_right_id) REFERENCES players (id);
