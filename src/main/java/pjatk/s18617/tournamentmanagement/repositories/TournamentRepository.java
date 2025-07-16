package pjatk.s18617.tournamentmanagement.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pjatk.s18617.tournamentmanagement.model.Game;
import pjatk.s18617.tournamentmanagement.model.Tournament;

import java.util.List;
import java.util.Optional;

public interface TournamentRepository extends JpaRepository<Tournament, Long>, JpaSpecificationExecutor<Tournament> {
    List<Tournament> findByGame(Game game);

    List<Tournament> findByGame_Id(Long id);

    @EntityGraph(attributePaths = {"userOwner", "game", "location", "teamRegistrations.team", "matches" +
            ".tournamentTeam1.team", "matches.tournamentTeam2.team", "matches.tournamentTeamWinner.team",
            "announcements", "usersManaging"})
    Optional<Tournament> findWholeById(Long id);

}