CREATE TABLE teams
(
    id              BIGINT NOT NULL,
    name            VARCHAR(255),
    player_left_id  BIGINT,
    player_right_id BIGINT,
    level           INTEGER,
    score           INTEGER,
    lives           INTEGER,
    CONSTRAINT pk_teams PRIMARY KEY (id)
);

ALTER TABLE teams
    ADD CONSTRAINT FK_TEAMS_ON_PLAYER_LEFT FOREIGN KEY (player_left_id) REFERENCES players (id);

ALTER TABLE teams
    ADD CONSTRAINT FK_TEAMS_ON_PLAYER_RIGHT FOREIGN KEY (player_right_id) REFERENCES players (id);