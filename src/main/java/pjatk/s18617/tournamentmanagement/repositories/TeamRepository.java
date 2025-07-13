package pjatk.s18617.tournamentmanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pjatk.s18617.tournamentmanagement.model.Team;
import pjatk.s18617.tournamentmanagement.model.Tournament;
import pjatk.s18617.tournamentmanagement.model.User;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {

    /**
     * Retrieves a list of teams owned by the specified user that are not registered in the given tournament, sorted
     * alphabetically by team name.
     *
     * <p>
     * This method performs a query to find all {@link Team} entities where the owner matches the provided
     * {@link User} and the team is not yet registered in the specified {@link Tournament}. The resulting list is
     * ordered by the team's name in ascending order.
     * </p>
     *
     * @param tournament the {@link Tournament} for which team registration is checked (must not be null)
     * @param user       the {@link User} who owns the teams to be retrieved (must not be null)
     * @return a list of {@link Team} entities owned by the user and not registered in the tournament, sorted by team
     * name in ascending order
     */
    @Query("""
            SELECT t FROM Team t
            WHERE t.userOwner = :user
              AND NOT EXISTS (
                  SELECT tt FROM TournamentTeam tt
                  WHERE tt.tournament = :tournament
                    AND tt.team = t
              )
            ORDER BY t.name ASC
            """)
    List<Team> findTeamsOwnedByUserNotRegisteredInTournamentOrderByName(
            @Param("tournament") Tournament tournament,
            @Param("user") User user
    );

}