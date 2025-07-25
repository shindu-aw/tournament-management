package pjatk.s18617.tournamentmanagement.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import pjatk.s18617.tournamentmanagement.model.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("h2-test")
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldCRUD() {
        // create
        User user = User.builder()
                .username("username")
                .password("password")
                .build();
        User saved = userRepository.save(user);
        assertNotNull(saved);

        // read
        Optional<User> found = userRepository.findById(saved.getId());
        assertTrue(found.isPresent());

        // update
        saved.setUsername("username 2");
        User updated = userRepository.save(saved);
        assertEquals("username 2", updated.getUsername());

        // delete
        userRepository.delete(updated);
        Optional<User> deleted = userRepository.findById(updated.getId());
        assertFalse(deleted.isPresent());
    }

}