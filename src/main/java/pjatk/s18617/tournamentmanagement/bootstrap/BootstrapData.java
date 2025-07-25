package pjatk.s18617.tournamentmanagement.bootstrap;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
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
@Profile({"dev", "bootstrap-data"})
public class BootstrapData implements CommandLineRunner {

    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final LocationRepository locationRepository;
    private final TournamentRepository tournamentRepository;
    private final TeamRepository teamRepository;
    private final TournamentTeamRepository tournamentTeamRepository;
    private final MatchRepository matchRepository;
    private final TeamUserRepository teamUserRepository;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        loadData();
    }

    private void loadData() {
        if (gameRepository.count() == 0) {

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            User admin = User.builder()
                    .username("admin")
                    .password(encoder.encode("admin"))
                    .description("Test admin opis.")
                    .role(Role.ADMIN)
                    .build();
            User user1 = User.builder()
                    .username("test1")
                    .password(encoder.encode("test1"))
                    .description("Test opis.")
                    .build();
            User user2 = User.builder()
                    .username("test2")
                    .password(encoder.encode("test2"))
                    .description("Test opis 2.")
                    .build();
            User user3 = User.builder()
                    .username("test3")
                    .password(encoder.encode("test3"))
                    .description("Test opis 3.")
                    .build();
            userRepository.saveAllAndFlush(Arrays.asList(admin, user1, user2, user3));


            Game cs2 = Game.builder()
                    .name("Counter Strike 2")
                    .description("Counter Strike 2 opis.")
                    .build();
            Game dota2 = Game.builder()
                    .name("Dota 2")
                    .description("Dota 2 opis.")
                    .build();
            Game r6 = Game.builder()
                    .name("Rainbow 6 Siege")
                    .description("Rainbow 6 Siege opis.")
                    .build();
            gameRepository.saveAllAndFlush(Arrays.asList(cs2, dota2, r6));


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
            Team teamAdmin = Team.builder()
                    .name("TEAM ADMIN")
                    .description("Opis drużyny TEAM ADMIN.")
                    .secretCode("12345678")
                    .userOwner(admin)
                    .build();
            teamRepository.saveAllAndFlush(Arrays.asList(team1, team2, team3, team4, team5, team6, team7, team8,
                    teamAdmin));

            TeamUser teamUser = TeamUser.builder()
                    .game(cs2)
                    .user(user1)
                    .team(team1)
                    .build();
            TeamUser teamUser2 = TeamUser.builder()
                    .game(dota2)
                    .user(user1)
                    .team(team1)
                    .build();
            TeamUser teamUser3 = TeamUser.builder()
                    .game(cs2)
                    .user(user2)
                    .team(team1)
                    .build();
            teamUserRepository.saveAllAndFlush(Arrays.asList(teamUser, teamUser2, teamUser3));

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
                    .name("Turniej 1 - Turniejowanie")
                    .description("Opis pierwszego turnieju. ĄąŻżŁł")
                    .startDate(LocalDate.now())
                    .endDate(LocalDate.now())
                    .joinSecretCode("12345678")
                    .manageSecretCode("87654321")
                    .userOwner(user1)
                    .game(cs2)
                    .location(location1)
                    .build();
            tournament1.getUsersManaging().add(user2);
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
            Tournament fillerTournament1 = Tournament.builder()
                    .name("Test Turniej 1")
                    .description("Opis test turnieju.")
                    .startDate(LocalDate.parse("2025-07-01", formatter))
                    .endDate(LocalDate.parse("2025-07-02", formatter))
                    .joinSecretCode("12345678")
                    .manageSecretCode("87654321")
                    .userOwner(user1)
                    .game(cs2)
                    .build();
            Tournament fillerTournament2 = Tournament.builder()
                    .name("Test Turniej 2")
                    .description("Opis test turnieju.")
                    .startDate(LocalDate.parse("2025-07-01", formatter))
                    .endDate(LocalDate.parse("2025-07-02", formatter))
                    .joinSecretCode("12345678")
                    .manageSecretCode("87654321")
                    .userOwner(user1)
                    .game(cs2)
                    .build();
            Tournament fillerTournament3 = Tournament.builder()
                    .name("Test Turniej 3")
                    .description("Opis test turnieju.")
                    .startDate(LocalDate.parse("2025-07-02", formatter))
                    .endDate(LocalDate.parse("2025-07-03", formatter))
                    .joinSecretCode("12345678")
                    .manageSecretCode("87654321")
                    .userOwner(user1)
                    .game(cs2)
                    .build();
            Tournament fillerTournament4 = Tournament.builder()
                    .name("Test Turniej 4")
                    .description("Opis test turnieju.")
                    .startDate(LocalDate.parse("2025-11-01", formatter))
                    .joinSecretCode("12345678")
                    .manageSecretCode("87654321")
                    .userOwner(user1)
                    .game(cs2)
                    .build();
            Tournament fillerTournament5 = Tournament.builder()
                    .name("Test Turniej 5")
                    .description("Opis test turnieju.")
                    .startDate(LocalDate.parse("2025-11-02", formatter))
                    .joinSecretCode("12345678")
                    .manageSecretCode("87654321")
                    .userOwner(user1)
                    .game(cs2)
                    .build();
            Tournament fillerTournament6 = Tournament.builder()
                    .name("Test Turniej 6")
                    .description("Opis test turnieju.")
                    .startDate(LocalDate.parse("2025-11-02", formatter))
                    .joinSecretCode("12345678")
                    .manageSecretCode("87654321")
                    .userOwner(user1)
                    .game(cs2)
                    .build();
            Tournament fillerTournament7 = Tournament.builder()
                    .name("Test Turniej 7")
                    .description("Opis test turnieju.")
                    .startDate(LocalDate.parse("2025-01-02", formatter))
                    .endDate(LocalDate.parse("2025-01-05", formatter))
                    .joinSecretCode("12345678")
                    .manageSecretCode("87654321")
                    .userOwner(user1)
                    .game(cs2)
                    .build();
            Tournament fillerTournament8 = Tournament.builder()
                    .name("Test Turniej 8")
                    .description("Opis test turnieju.")
                    .startDate(LocalDate.parse("2025-01-02", formatter))
                    .endDate(LocalDate.parse("2025-01-12", formatter))
                    .finished(true)
                    .joinSecretCode("12345678")
                    .manageSecretCode("87654321")
                    .userOwner(user1)
                    .game(cs2)
                    .build();
            Tournament fillerTournament9 = Tournament.builder()
                    .name("Test Turniej 9")
                    .description("Opis test turnieju.")
                    .startDate(LocalDate.parse("2025-04-22", formatter))
                    .endDate(LocalDate.parse("2025-04-22", formatter))
                    .finished(true)
                    .joinSecretCode("12345678")
                    .manageSecretCode("87654321")
                    .userOwner(user1)
                    .game(cs2)
                    .build();
            Tournament fillerTournament10 = Tournament.builder()
                    .name("Test Turniej 10")
                    .description("Opis test turnieju.")
                    .startDate(LocalDate.parse("2025-04-22", formatter))
                    .endDate(LocalDate.parse("2025-04-28", formatter))
                    .finished(true)
                    .joinSecretCode("12345678")
                    .manageSecretCode("87654321")
                    .userOwner(user1)
                    .game(cs2)
                    .build();
            Tournament fillerTournament11 = Tournament.builder()
                    .name("Test Turniej 11")
                    .description("Opis test turnieju.")
                    .startDate(LocalDate.parse("2025-04-22", formatter))
                    .endDate(LocalDate.parse("2025-04-29", formatter))
                    .joinSecretCode("12345678")
                    .manageSecretCode("87654321")
                    .userOwner(user1)
                    .game(cs2)
                    .build();
            Tournament fillerTournament12 = Tournament.builder()
                    .name("Test Turniej 12")
                    .description("Opis test turnieju.")
                    .startDate(LocalDate.parse("2025-10-11", formatter))
                    .endDate(LocalDate.parse("2025-10-20", formatter))
                    .joinSecretCode("12345678")
                    .manageSecretCode("87654321")
                    .userOwner(user2)
                    .game(cs2)
                    .build();
            Tournament fillerTournament13 = Tournament.builder()
                    .name("Test Turniej 13")
                    .description("Opis test turnieju.")
                    .startDate(LocalDate.parse("2025-05-11", formatter))
                    .endDate(LocalDate.parse("2025-05-20", formatter))
                    .joinSecretCode("12345678")
                    .manageSecretCode("87654321")
                    .userOwner(user2)
                    .game(cs2)
                    .build();
            tournamentRepository.saveAllAndFlush(Arrays.asList(tournament1, tournament2, tournament3,
                    fillerTournament1, fillerTournament2, fillerTournament3, fillerTournament4, fillerTournament5,
                    fillerTournament6, fillerTournament7, fillerTournament8, fillerTournament9, fillerTournament10,
                    fillerTournament11, fillerTournament12, fillerTournament13));

            TournamentTeam tournamentTeam1 = TournamentTeam.builder()
                    .tournament(tournament1)
                    .team(team1)
                    .build();
            TournamentTeam tournamentTeam2 = TournamentTeam.builder()
                    .tournament(tournament1)
                    .team(team2)
                    .build();
            TournamentTeam tournamentTeam3 = TournamentTeam.builder()
                    .tournament(tournament1)
                    .team(team3)
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
                    .build();
            Match match2 = Match.builder()
                    .tournament(tournament1)
                    .tournamentTeam1(tournamentTeam1)
                    .tournamentTeam2(tournamentTeam3)
                    .date(LocalDate.parse("2025-07-02", formatter))
                    .team1Score(16)
                    .team2Score(3)
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
