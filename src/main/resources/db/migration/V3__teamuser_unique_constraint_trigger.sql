CREATE TRIGGER trg_team_user_before_insert_unique_check
    BEFORE INSERT
    ON team_user
    FOR EACH ROW
BEGIN
    IF EXISTS (SELECT 1
               FROM team_user
               WHERE team_id = NEW.team_id AND user_id = NEW.user_id AND game_id = NEW.game_id) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'User is already a member of this team assigned to this game.';
    END IF;
END;