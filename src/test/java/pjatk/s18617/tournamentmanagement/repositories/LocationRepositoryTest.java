package pjatk.s18617.tournamentmanagement.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import pjatk.s18617.tournamentmanagement.model.Location;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("h2-test")
@DataJpaTest
class LocationRepositoryTest {

    @Autowired
    private LocationRepository locationRepository;

    @Test
    void shouldCRUD() {
        // create
        Location location = Location.builder()
                .country("Country")
                .postalCode("12345")
                .city("City")
                .street("Street")
                .houseNumber("12")
                .build();
        Location saved = locationRepository.save(location);
        assertNotNull(saved);

        // read
        Optional<Location> found = locationRepository.findById(saved.getId());
        assertTrue(found.isPresent());

        // update
        saved.setCountry("Country 2");
        Location updated = locationRepository.save(saved);
        assertEquals("Country 2", updated.getCountry());

        // delete
        locationRepository.delete(updated);
        Optional<Location> deleted = locationRepository.findById(saved.getId());
        assertFalse(deleted.isPresent());
    }

}