DELIMITER //

CREATE FUNCTION `manage_tournaments_by_date`(threshold_date DATE)
    RETURNS INT
    DETERMINISTIC
BEGIN
    DECLARE tournaments_deleted INT DEFAULT 0;

    -- 1. Change tournaments' end_date to today and finished to true if their end_date is at least six months prior to today
    UPDATE tournament
    SET end_date = CURDATE(),
        finished = TRUE
    WHERE start_date <= DATE_SUB(CURDATE(), INTERVAL 6 MONTH)
      AND end_date IS NULL
      AND finished = FALSE;

    -- 2. Mark tournaments as finished if their end_date is at least three months prior to today
    UPDATE tournament
    SET finished = TRUE
    WHERE end_date <= DATE_SUB(CURDATE(), INTERVAL 3 MONTH)
      AND finished = FALSE;

    -- 3. Delete finished tournaments and related data if end_date is older than p_threshold_date

    -- Collect tournament IDs to be deleted
    -- This temporary table will also store the location_id for later deletion of locations
    DROP TEMPORARY TABLE IF EXISTS temp_tournaments_to_delete;
    CREATE TEMPORARY TABLE temp_tournaments_to_delete
    (
        tournament_id BIGINT,
        location_id   BIGINT NULL -- location_id can be NULL
    );

    INSERT INTO temp_tournaments_to_delete (tournament_id, location_id)
    SELECT id, location_id
    FROM tournament
    WHERE finished = TRUE
      AND end_date < threshold_date;

    -- Delete child data first
    DELETE FROM announcement WHERE tournament_id IN (SELECT tournament_id FROM temp_tournaments_to_delete);
    DELETE FROM match_entry WHERE tournament_id IN (SELECT tournament_id FROM temp_tournaments_to_delete);
    DELETE FROM tournament_team WHERE tournament_id IN (SELECT tournament_id FROM temp_tournaments_to_delete);
    DELETE FROM tournament_user WHERE tournament_id IN (SELECT tournament_id FROM temp_tournaments_to_delete);

    -- Delete the tournament records themselves
    DELETE FROM tournament WHERE id IN (SELECT tournament_id FROM temp_tournaments_to_delete);

    -- Finally, delete the associated locations.
    -- This step must happen AFTER the tournaments are deleted to avoid foreign key constraint violations from the tournament table.
    -- Distinct on location_id is used to ensure we only try to delete each location once.
    -- Distinct is used, but it shouldn't be necessary since the tournament_location constraint is unique.
    -- Thus, distinct here is used mainly for future-proofing in case the unique constraint is ever removed.
    DELETE
    FROM location
    WHERE id IN (SELECT DISTINCT location_id FROM temp_tournaments_to_delete WHERE location_id IS NOT NULL);


    -- Get the count of deleted tournaments
    SELECT COUNT(*) INTO tournaments_deleted FROM temp_tournaments_to_delete;

    DROP TEMPORARY TABLE temp_tournaments_to_delete;

    RETURN tournaments_deleted;
END //

DELIMITER ;