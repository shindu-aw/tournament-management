package pjatk.s18617.tournamentmanagement.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import pjatk.s18617.tournamentmanagement.model.Game;
import pjatk.s18617.tournamentmanagement.model.Team;
import pjatk.s18617.tournamentmanagement.model.TeamUser;
import pjatk.s18617.tournamentmanagement.model.User;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("h2-test")
@DataJpaTest
class TeamUserRepositoryTest {

    @Autowired
    private TeamUserRepository teamUserRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Test
    void shouldCRUD() {
        User user = User.builder()
                .username("username")
                .password("password")
                .build();
        userRepository.save(user);
        Team team = Team.builder()
                .name("team")
                .description("description")
                .userOwner(user)
                .secretCode("12345678")
                .build();
        Team team2 = Team.builder()
                .name("team 2")
                .description("description 2")
                .userOwner(user)
                .secretCode("12345678")
                .build();
        teamRepository.saveAll(Arrays.asList(team, team2));
        Game game = Game.builder()
                .name("game")
                .description("description")
                .build();

        // create
        TeamUser teamUser = TeamUser.builder()
                .team(team)
                .game(game)
                .user(user)
                .build();
        TeamUser saved = teamUserRepository.save(teamUser);
        assertNotNull(saved);

        // read
        Optional<TeamUser> found = teamUserRepository.findById(saved.getId());
        assertTrue(found.isPresent());

        // update
        saved.setTeam(team2);
        TeamUser updated = teamUserRepository.save(saved);
        assertEquals(team2, updated.getTeam());

        // delete
        teamUserRepository.delete(updated);
        Optional<TeamUser> deleted = teamUserRepository.findById(updated.getId());
        assertFalse(deleted.isPresent());
    }

}