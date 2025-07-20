CREATE SEQUENCE announcement_seq INCREMENT BY 50 START WITH 1;

CREATE SEQUENCE game_seq INCREMENT BY 50 START WITH 1;

CREATE SEQUENCE link_seq INCREMENT BY 50 START WITH 1;

CREATE SEQUENCE location_seq INCREMENT BY 50 START WITH 1;

CREATE SEQUENCE match_seq INCREMENT BY 50 START WITH 1;

CREATE SEQUENCE team_seq INCREMENT BY 50 START WITH 1;

CREATE SEQUENCE team_user_seq INCREMENT BY 50 START WITH 1;

CREATE SEQUENCE tournament_seq INCREMENT BY 50 START WITH 1;

CREATE SEQUENCE tournament_team_seq INCREMENT BY 50 START WITH 1;

CREATE SEQUENCE user_seq INCREMENT BY 50 START WITH 1;

CREATE TABLE announcement
(
    id            BIGINT        NOT NULL,
    title         VARCHAR(50)   NOT NULL,
    `description` VARCHAR(1000) NOT NULL,
    tournament_id BIGINT        NOT NULL,
    creation_date datetime      NOT NULL,
    CONSTRAINT pk_announcement PRIMARY KEY (id)
);

CREATE TABLE game
(
    id            BIGINT      NOT NULL,
    name          VARCHAR(50) NOT NULL,
    `description` VARCHAR(1000) NULL,
    CONSTRAINT pk_game PRIMARY KEY (id)
);

CREATE TABLE link
(
    id      BIGINT        NOT NULL,
    name    VARCHAR(50)   NOT NULL,
    url     VARCHAR(2000) NOT NULL,
    team_id BIGINT        NOT NULL,
    CONSTRAINT pk_link PRIMARY KEY (id)
);

CREATE TABLE location
(
    id           BIGINT       NOT NULL,
    country      VARCHAR(100) NOT NULL,
    postal_code  VARCHAR(10)  NOT NULL,
    city         VARCHAR(100) NOT NULL,
    street       VARCHAR(100) NOT NULL,
    house_number VARCHAR(10)  NOT NULL,
    CONSTRAINT pk_location PRIMARY KEY (id)
);

CREATE TABLE match_entry
(
    id                   BIGINT NOT NULL,
    team_1_score         INT NULL,
    team_2_score         INT NULL,
    date                 date   NOT NULL,
    tournament_id        BIGINT NOT NULL,
    tournament_team_1_id BIGINT NULL,
    tournament_team_2_id BIGINT NULL,
    CONSTRAINT pk_match_entry PRIMARY KEY (id)
);

CREATE TABLE team
(
    id            BIGINT      NOT NULL,
    name          VARCHAR(50) NOT NULL,
    `description` VARCHAR(1000) NULL,
    secret_code   VARCHAR(8)  NOT NULL,
    user_owner_id BIGINT      NOT NULL,
    CONSTRAINT pk_team PRIMARY KEY (id)
);

CREATE TABLE team_user
(
    id        BIGINT NOT NULL,
    team_id   BIGINT NULL,
    user_id   BIGINT NULL,
    game_id   BIGINT NULL,
    join_date date   NOT NULL,
    CONSTRAINT pk_team_user PRIMARY KEY (id)
);

CREATE TABLE tournament
(
    id                 BIGINT      NOT NULL,
    name               VARCHAR(50) NOT NULL,
    `description`      VARCHAR(1000) NULL,
    start_date         date        NOT NULL,
    end_date           date NULL,
    finished           BIT(1)      NOT NULL,
    join_secret_code   VARCHAR(8)  NOT NULL,
    manage_secret_code VARCHAR(8)  NOT NULL,
    user_owner_id      BIGINT      NOT NULL,
    location_id        BIGINT NULL,
    game_id            BIGINT NULL,
    CONSTRAINT pk_tournament PRIMARY KEY (id)
);

CREATE TABLE tournament_team
(
    id            BIGINT NOT NULL,
    score_sum     INT NULL,
    tournament_id BIGINT NULL,
    team_id       BIGINT NULL,
    CONSTRAINT pk_tournament_team PRIMARY KEY (id)
);

CREATE TABLE tournament_user
(
    tournament_id BIGINT NOT NULL,
    user_id       BIGINT NOT NULL
);

CREATE TABLE user
(
    id            BIGINT       NOT NULL,
    username      VARCHAR(20)  NOT NULL,
    password      VARCHAR(255) NOT NULL,
    `description` VARCHAR(1000) NULL,
    `role`        VARCHAR(5)   NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

ALTER TABLE game
    ADD CONSTRAINT uc_game_name UNIQUE (name);

ALTER TABLE tournament
    ADD CONSTRAINT uc_tournament_location UNIQUE (location_id);

ALTER TABLE user
    ADD CONSTRAINT uc_user_username UNIQUE (username);

ALTER TABLE announcement
    ADD CONSTRAINT FK_ANNOUNCEMENT_ON_TOURNAMENT FOREIGN KEY (tournament_id) REFERENCES tournament (id);

ALTER TABLE link
    ADD CONSTRAINT FK_LINK_ON_TEAM FOREIGN KEY (team_id) REFERENCES team (id);

ALTER TABLE match_entry
    ADD CONSTRAINT FK_MATCH_ENTRY_ON_TOURNAMENT FOREIGN KEY (tournament_id) REFERENCES tournament (id);

ALTER TABLE match_entry
    ADD CONSTRAINT FK_MATCH_ENTRY_ON_TOURNAMENT_TEAM_1 FOREIGN KEY (tournament_team_1_id) REFERENCES tournament_team (id);

ALTER TABLE match_entry
    ADD CONSTRAINT FK_MATCH_ENTRY_ON_TOURNAMENT_TEAM_2 FOREIGN KEY (tournament_team_2_id) REFERENCES tournament_team (id);

ALTER TABLE team
    ADD CONSTRAINT FK_TEAM_ON_USER_OWNER FOREIGN KEY (user_owner_id) REFERENCES user (id);

ALTER TABLE team_user
    ADD CONSTRAINT FK_TEAM_USER_ON_GAME FOREIGN KEY (game_id) REFERENCES game (id);

ALTER TABLE team_user
    ADD CONSTRAINT FK_TEAM_USER_ON_TEAM FOREIGN KEY (team_id) REFERENCES team (id);

ALTER TABLE team_user
    ADD CONSTRAINT FK_TEAM_USER_ON_USER FOREIGN KEY (user_id) REFERENCES user (id);

ALTER TABLE tournament
    ADD CONSTRAINT FK_TOURNAMENT_ON_GAME FOREIGN KEY (game_id) REFERENCES game (id);

ALTER TABLE tournament
    ADD CONSTRAINT FK_TOURNAMENT_ON_LOCATION FOREIGN KEY (location_id) REFERENCES location (id);

ALTER TABLE tournament
    ADD CONSTRAINT FK_TOURNAMENT_ON_USER_OWNER FOREIGN KEY (user_owner_id) REFERENCES user (id);

ALTER TABLE tournament_team
    ADD CONSTRAINT FK_TOURNAMENT_TEAM_ON_TEAM FOREIGN KEY (team_id) REFERENCES team (id);

ALTER TABLE tournament_team
    ADD CONSTRAINT FK_TOURNAMENT_TEAM_ON_TOURNAMENT FOREIGN KEY (tournament_id) REFERENCES tournament (id);

ALTER TABLE tournament_user
    ADD CONSTRAINT fk_touuse_on_tournament FOREIGN KEY (tournament_id) REFERENCES tournament (id);

ALTER TABLE tournament_user
    ADD CONSTRAINT fk_touuse_on_user FOREIGN KEY (user_id) REFERENCES user (id);