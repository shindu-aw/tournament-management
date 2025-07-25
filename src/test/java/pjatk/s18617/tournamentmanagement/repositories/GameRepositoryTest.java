package pjatk.s18617.tournamentmanagement.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import pjatk.s18617.tournamentmanagement.model.Game;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("h2-test")
@DataJpaTest
class GameRepositoryTest {

    @Autowired
    private GameRepository gameRepository;

    @Test
    void shouldCRUD() {
        // create
        Game game = Game.builder()
                .name("Counter Strike")
                .description("Counter Strike description.")
                .build();
        Game saved = gameRepository.save(game);
        assertNotNull(saved);

        // read
        Optional<Game> found = gameRepository.findById(saved.getId());
        assertTrue(found.isPresent());

        // update
        saved.setName("Counter Strike 2");
        Game updated = gameRepository.save(saved);
        assertEquals("Counter Strike 2", updated.getName());

        // delete
        gameRepository.delete(updated);
        Optional<Game> deleted = gameRepository.findById(saved.getId());
        assertFalse(deleted.isPresent());
    }
}