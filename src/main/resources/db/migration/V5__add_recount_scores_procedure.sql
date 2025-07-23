DELIMITER //

CREATE PROCEDURE recount_tournament_team_scores(IN tournament_id_in BIGINT)
BEGIN
    -- reset score_sum for all tournament_teams belonging to the specified tournament
    UPDATE tournament_team
    SET score_sum = 0
    WHERE tournament_id = tournament_id_in;

    -- recalculate score_sum by summing all team_1_scores and team_2_scores for each tournament_team
    UPDATE tournament_team tt
    SET tt.score_sum = (SELECT IFNULL(SUM(IF(me.tournament_team_1_id = tt.id, me.team_1_score, 0)), 0) +
                               IFNULL(SUM(IF(me.tournament_team_2_id = tt.id, me.team_2_score, 0)), 0)
                        FROM match_entry me
                        WHERE (tt.id = me.tournament_team_1_id OR tt.id = me.tournament_team_2_id)
                          AND me.tournament_id = tournament_id_in)
    WHERE tt.tournament_id = tournament_id_in;

END //

DELIMITER ;