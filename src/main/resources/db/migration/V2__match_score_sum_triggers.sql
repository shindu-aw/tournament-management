DELIMITER //


CREATE TRIGGER trg_match_insert_score_update
    AFTER INSERT
    ON match_entry
    FOR EACH ROW
BEGIN
    UPDATE tournament_team
    SET score_sum = score_sum + NEW.team_1_score
    WHERE id = NEW.tournament_team_1_id;

    UPDATE tournament_team
    SET score_sum = score_sum + NEW.team_2_score
    WHERE id = NEW.tournament_team_2_id;
END //


CREATE TRIGGER trg_match_update_score_update
    AFTER UPDATE
    ON match_entry
    FOR EACH ROW
BEGIN
    -- deduct old scores
    UPDATE tournament_team
    SET score_sum = score_sum - OLD.team_1_score
    WHERE id = OLD.tournament_team_1_id;

    UPDATE tournament_team
    SET score_sum = score_sum - OLD.team_2_score
    WHERE id = OLD.tournament_team_2_id;

    -- add new scores
    UPDATE tournament_team
    SET score_sum = score_sum + NEW.team_1_score
    WHERE id = NEW.tournament_team_1_id;

    UPDATE tournament_team
    SET score_sum = score_sum + NEW.team_2_score
    WHERE id = NEW.tournament_team_2_id;
END //


CREATE TRIGGER trg_match_delete_score_update
    AFTER DELETE
    ON match_entry
    FOR EACH ROW
BEGIN
    UPDATE tournament_team
    SET score_sum = score_sum - OLD.team_1_score
    WHERE id = OLD.tournament_team_1_id;

    UPDATE tournament_team
    SET score_sum = score_sum - OLD.team_2_score
    WHERE id = OLD.tournament_team_2_id;
END //


DELIMITER;