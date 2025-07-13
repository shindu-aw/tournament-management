package pjatk.s18617.tournamentmanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pjatk.s18617.tournamentmanagement.model.Game;
import pjatk.s18617.tournamentmanagement.model.Team;
import pjatk.s18617.tournamentmanagement.model.User;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findAllByOrderByNameAsc();

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