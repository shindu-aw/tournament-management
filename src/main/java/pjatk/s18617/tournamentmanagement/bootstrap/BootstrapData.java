package pjatk.s18617.tournamentmanagement.bootstrap;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pjatk.s18617.tournamentmanagement.model.Game;
import pjatk.s18617.tournamentmanagement.model.Location;
import pjatk.s18617.tournamentmanagement.model.Tournament;
import pjatk.s18617.tournamentmanagement.model.User;
import pjatk.s18617.tournamentmanagement.repositories.GameRepository;
import pjatk.s18617.tournamentmanagement.repositories.LocationRepository;
import pjatk.s18617.tournamentmanagement.repositories.TournamentRepository;
import pjatk.s18617.tournamentmanagement.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {

    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final LocationRepository locationRepository;
    private final TournamentRepository tournamentRepository;

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        loadData();
    }

    private void loadData() {
        if (gameRepository.count() == 0) {

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            User user1 = User.builder()
                    .username("testUser")
                    .password(encoder.encode("testPassword"))
                    .description("Test description.")
                    .build();
            User user2 = User.builder()
                    .username("testUser2")
                    .password(encoder.encode("testPassword2"))
                    .description("Test description 2.")
                    .build();
            userRepository.saveAllAndFlush(Arrays.asList(user1, user2));


            Game cs2 = Game.builder()
                    .name("Counter Strike 2")
                    .description("Counter Strike 2 description.")
                    .build();
            Game dota2 = Game.builder()
                    .name("Dota 2")
                    .description("Dota 2 description.")
                    .build();
            gameRepository.saveAllAndFlush(Arrays.asList(cs2, dota2));

            Location location1 = Location.builder()
                    .country("Polska")
                    .postalCode("00-001")
                    .city("Warszawa")
                    .street("Uliczna")
                    .houseNumber("12b")
                    .build();
            Location location2 = Location.builder()
                    .country("Polska")
                    .postalCode("80-001")
                    .city("Gdańsk")
                    .street("Bałtykowa")
                    .houseNumber("21")
                    .build();
            locationRepository.saveAllAndFlush(Arrays.asList(location1, location2));

            Tournament tournament1 = Tournament.builder()
                    .name("Tournament 1")
                    .description("Tournament 1 description.")
                    .startDate(LocalDateTime.now())
                    .endDate(LocalDateTime.now())
                    .joinSecretCode("12345678")
                    .manageSecretCode("87654321")
                    .userOwner(user1)
                    .game(cs2)
                    .location(location1)
                    .build();
            Tournament tournament2 = Tournament.builder()
                    .name("Tournament 2")
                    .description("Tournament 2 description.")
                    .startDate(LocalDateTime.now())
                    .endDate(LocalDateTime.now())
                    .joinSecretCode("12345678")
                    .manageSecretCode("87654321")
                    .userOwner(user2)
                    .game(dota2)
                    .location(location2)
                    .build();
            Tournament tournament3 = Tournament.builder()
                    .name("Tournament 3")
                    .description("Tournament 3 description.")
                    .startDate(LocalDateTime.now())
                    .endDate(LocalDateTime.now())
                    .joinSecretCode("12345678")
                    .manageSecretCode("87654321")
                    .userOwner(user2)
                    .game(dota2)
                    // without location
                    .build();
            tournamentRepository.saveAllAndFlush(Arrays.asList(tournament1, tournament2, tournament3));



        }
    }

}
