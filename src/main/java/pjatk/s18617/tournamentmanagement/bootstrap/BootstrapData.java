package pjatk.s18617.tournamentmanagement.bootstrap;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pjatk.s18617.tournamentmanagement.model.*;
import pjatk.s18617.tournamentmanagement.repositories.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {

    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final LocationRepository locationRepository;
    private final TournamentRepository tournamentRepository;
    private final TeamRepository teamRepository;
    private final TournamentTeamRepository tournamentTeamRepository;
    private final MatchRepository matchRepository;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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


            Team team1 = Team.builder()
                    .name("Drużyna 1")
                    .description("Opis drużyny 1.")
                    .secretCode("12345678")
                    .userOwner(user1)
                    .build();
            Team team2 = Team.builder()
                    .name("Drużyna 2")
                    .description("Opis drużyny 2.")
                    .secretCode("12345678")
                    .userOwner(user2)
                    .build();
            Team team3 = Team.builder()
                    .name("Drużyna 3")
                    .description("Opis drużyny 3.")
                    .secretCode("12345678")
                    .userOwner(user1)
                    .build();
            Team team4 = Team.builder()
                    .name("Drużyna 4")
                    .description("Opis drużyny 4.")
                    .secretCode("12345678")
                    .userOwner(user2)
                    .build();
            Team team5 = Team.builder()
                    .name("Drużyna 5")
                    .description("Opis drużyny 5.")
                    .secretCode("12345678")
                    .userOwner(user1)
                    .build();
            Team team6 = Team.builder()
                    .name("Drużyna 6")
                    .description("Opis drużyny 6.")
                    .secretCode("12345678")
                    .userOwner(user2)
                    .build();
            Team team7 = Team.builder()
                    .name("Drużyna 7")
                    .description("Opis drużyny 7.")
                    .secretCode("12345678")
                    .userOwner(user1)
                    .build();
            Team team8 = Team.builder()
                    .name("Drużyna 8")
                    .description("Opis drużyny 8.")
                    .secretCode("12345678")
                    .userOwner(user2)
                    .build();
            teamRepository.saveAllAndFlush(Arrays.asList(team1, team2, team3, team4, team5, team6, team7, team8));

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
                    .name("Turniej 1")
                    .description("Opis pierwszego turnieju. ĄąŻżŁł")
                    .startDate(LocalDate.now())
                    .endDate(LocalDate.now())
                    .joinSecretCode("12345678")
                    .manageSecretCode("87654321")
                    .userOwner(user1)
                    .game(cs2)
                    .location(location1)
                    .build();
            Tournament tournament2 = Tournament.builder()
                    .name("Turniej 2")
                    .description("Opis drugiego turnieju.")
                    .startDate(LocalDate.now())
                    .endDate(LocalDate.now())
                    .joinSecretCode("12345678")
                    .manageSecretCode("87654321")
                    .userOwner(user2)
                    .game(dota2)
                    .location(location2)
                    .build();
            Tournament tournament3 = Tournament.builder()
                    .name("Turniej 3")
                    .description("Opis trzeciego turnieju.")
                    .startDate(LocalDate.now())
                    // without end date
                    .joinSecretCode("12345678")
                    .manageSecretCode("87654321")
                    .userOwner(user2)
                    .game(dota2)
                    // without location
                    .build();
            tournamentRepository.saveAllAndFlush(Arrays.asList(tournament1, tournament2, tournament3));

            TournamentTeam tournamentTeam1 = TournamentTeam.builder()
                    .tournament(tournament1)
                    .team(team1)
                    .scoreSum(28)
                    .build();
            TournamentTeam tournamentTeam2 = TournamentTeam.builder()
                    .tournament(tournament1)
                    .team(team2)
                    .scoreSum(30)
                    .build();
            TournamentTeam tournamentTeam3 = TournamentTeam.builder()
                    .tournament(tournament1)
                    .team(team3)
                    .scoreSum(3)
                    .build();
            TournamentTeam tournamentTeam4 = TournamentTeam.builder()
                    .tournament(tournament1)
                    .team(team4)
                    .build();
            tournamentTeamRepository.saveAllAndFlush(Arrays.asList(tournamentTeam1, tournamentTeam2, tournamentTeam3,
                    tournamentTeam4));


            Match match1 = Match.builder()
                    .tournament(tournament1)
                    .tournamentTeam1(tournamentTeam1)
                    .tournamentTeam2(tournamentTeam2)
                    .date(LocalDate.parse("2025-07-01", formatter))
                    .team1Score(12)
                    .team2Score(30)
                    .tournamentTeamWinner(tournamentTeam2)
                    .build();
            Match match2 = Match.builder()
                    .tournament(tournament1)
                    .tournamentTeam1(tournamentTeam1)
                    .tournamentTeam2(tournamentTeam3)
                    .date(LocalDate.parse("2025-07-02", formatter))
                    .team1Score(16)
                    .team2Score(3)
                    .tournamentTeamWinner(tournamentTeam1)
                    .build();
            Match match3 = Match.builder()
                    .tournament(tournament1)
                    .tournamentTeam1(tournamentTeam1)
                    .tournamentTeam2(tournamentTeam3)
                    .date(LocalDate.parse("2025-07-03", formatter))
                    .build();
            matchRepository.saveAllAndFlush(Arrays.asList(match1, match2, match3));


        }
    }

}
