package pjatk.s18617.tournamentmanagement.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import pjatk.s18617.tournamentmanagement.model.Link;
import pjatk.s18617.tournamentmanagement.model.Team;
import pjatk.s18617.tournamentmanagement.model.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("h2-test")
@DataJpaTest
class LinkRepositoryTest {

    @Autowired
    private LinkRepository linkRepository;

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
                .build();
        teamRepository.save(team);

        // create
        Link link = Link.builder()
                .name("link")
                .url("https://www.google.com/")
                .team(team)
                .build();
        Link saved = linkRepository.save(link);
        assertNotNull(saved);

        // read
        Optional<Link> found = linkRepository.findById(saved.getId());
        assertTrue(found.isPresent());

        // update
        saved.setName("link 2");
        Link updated = linkRepository.save(saved);
        assertEquals("link 2", updated.getName());

        // delete
        linkRepository.delete(saved);
        Optional<Link> deleted = linkRepository.findById(saved.getId());
        assertFalse(deleted.isPresent());
    }

}