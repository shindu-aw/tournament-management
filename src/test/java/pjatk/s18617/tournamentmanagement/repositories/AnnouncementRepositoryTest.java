package pjatk.s18617.tournamentmanagement.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import pjatk.s18617.tournamentmanagement.model.Announcement;
import pjatk.s18617.tournamentmanagement.model.Tournament;
import pjatk.s18617.tournamentmanagement.model.User;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("h2-test")
@DataJpaTest
class AnnouncementRepositoryTest {

    @Autowired
    private AnnouncementRepository announcementRepository;

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
        Tournament tournament = Tournament.builder()
                .name("Tournament 1")
                .description("Description 1")
                .startDate(LocalDate.now())
                .userOwner(user)
                .build();
        tournamentRepository.save(tournament);

        // create
        Announcement announcement = Announcement.builder()
                .title("title")
                .description("description")
                .tournament(tournament)
                .build();
        Announcement saved = announcementRepository.save(announcement);
        assertNotNull(saved);

        // read
        Optional<Announcement> found = announcementRepository.findById(saved.getId());
        assertTrue(found.isPresent());

        // update
        saved.setTitle("title 2");
        Announcement updated = announcementRepository.save(saved);
        assertEquals("title 2", updated.getTitle());

        // delete
        announcementRepository.delete(updated);
        Optional<Announcement> deleted = announcementRepository.findById(saved.getId());
        assertFalse(deleted.isPresent());
    }

}