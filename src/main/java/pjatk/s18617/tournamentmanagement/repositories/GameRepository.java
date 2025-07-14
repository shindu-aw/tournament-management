package pjatk.s18617.tournamentmanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pjatk.s18617.tournamentmanagement.model.Game;
import pjatk.s18617.tournamentmanagement.model.Team;
import pjatk.s18617.tournamentmanagement.model.TeamUser;
import pjatk.s18617.tournamentmanagement.model.User;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findAllByOrderByNameAsc();

    /**
     * Retrieves a list of {@link Game} entities for which the specified {@link User} is not registered with the
     * given {@link Team}.
     *
     * <p>
     * <b>Typical usage:</b> In a team application form, to present a list of games to which a user can still be
     * registered to a team.
     * </p>
     *
     * @param team the {@link Team} for which to check user registrations
     * @param user the {@link User} to check for existing game registrations
     * @return a list of {@link Game} entities not yet registered for the given user and team, sorted by game name in
     * ascending order
     * @see TeamUser
     * @see Game
     */
    @Query("""
            SELECT g FROM Game g
            WHERE NOT EXISTS (
                SELECT tu FROM TeamUser tu
                WHERE tu.team = :team
                    AND tu.user = :user
                    AND tu.game = g
            )
            ORDER BY g.name
            """)
    List<Game> findGamesUserNotRegisteredForTeam(@Param("team") Team team, @Param("user") User user);
}