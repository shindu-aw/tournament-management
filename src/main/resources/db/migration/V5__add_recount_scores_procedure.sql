DELIMITER //
//
CREATE PROCEDURE recount_tournament_team_scores(IN tournament_id_in BIGINT)
BEGIN
    -- reset score_sum for all tournaments belonging to the specified tournament
    UPDATE tournament_team
    SET score_sum = 0
    WHERE tournament_id = tournament_id_in;

    -- recalculate score_sum for team_1 scores
    UPDATE tournament_team tt
        JOIN match_entry me ON tt.id = me.tournament_team_1_id
    SET tt.score_sum = tt.score_sum + me.team_1_score
    WHERE me.tournament_id = tournament_id_in;

    -- recalculate score_sum for team_2 scores
    UPDATE tournament_team tt
        JOIN match_entry me ON tt.id = me.tournament_team_2_id
    SET tt.score_sum = tt.score_sum + me.team_2_score
    WHERE me.tournament_id = tournament_id_in;
END//
DELIMITER ;

