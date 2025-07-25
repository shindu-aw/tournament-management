package pjatk.s18617.tournamentmanagement.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import pjatk.s18617.tournamentmanagement.model.Tournament;
import pjatk.s18617.tournamentmanagement.model.User;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("h2-test")
@DataJpaTest
class TournamentRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TournamentRepository tournamentRepository;

    @Test
    void shouldCRUD() {
        User user = User.builder()
                .username("username")
                .password("password")
                .build();
        userRepository.save(user);

        // create
        Tournament tournament = Tournament.builder()
                .name("Tournament 1")
                .description("Description 1")
                .startDate(LocalDate.now())
                .userOwner(user)
                .build();
        Tournament saved = tournamentRepository.save(tournament);
        assertNotNull(saved);

        // read
        Optional<Tournament> found = tournamentRepository.findById(saved.getId());
        assertNotNull(found);

        // update
        saved.setName("Tournament 2");
        Tournament updated = tournamentRepository.save(saved);
        assertEquals("Tournament 2", updated.getName());

        // delete
        tournamentRepository.delete(updated);
        Optional<Tournament> deleted = tournamentRepository.findById(updated.getId());
        assertFalse(deleted.isPresent());
    }

}