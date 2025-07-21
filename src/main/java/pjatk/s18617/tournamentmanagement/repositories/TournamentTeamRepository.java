package pjatk.s18617.tournamentmanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import pjatk.s18617.tournamentmanagement.model.TournamentTeam;

public interface TournamentTeamRepository extends JpaRepository<TournamentTeam, Long> {

    @Procedure(value = "recount_tournament_team_scores") // migration V5__add_recount_scores_procedure.sql
    void recountTournamentTeamScores(Long id);

}