package pjatk.s18617.tournamentmanagement.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import pjatk.s18617.tournamentmanagement.model.Team;
import pjatk.s18617.tournamentmanagement.model.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("h2-test")
@DataJpaTest
class TeamRepositoryTest {

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

        // create
        Team team = Team.builder()
                .name("team")
                .description("description")
                .userOwner(user)
                .build();
        Team saved = teamRepository.save(team);
        assertNotNull(saved);

        // read
        Optional<Team> found = teamRepository.findById(saved.getId());
        assertTrue(found.isPresent());

        // update
        saved.setName("team 2");
        Team updated = teamRepository.save(saved);
        assertEquals("team 2", updated.getName());

        // delete
        teamRepository.delete(updated);
        Optional<Team> deleted = teamRepository.findById(saved.getId());
        assertFalse(deleted.isPresent());
    }

}