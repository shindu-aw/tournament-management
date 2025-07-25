package pjatk.s18617.tournamentmanagement.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import pjatk.s18617.tournamentmanagement.MyTestContainerConfig;
import pjatk.s18617.tournamentmanagement.model.*;
import pjatk.s18617.tournamentmanagement.repositories.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(MyTestContainerConfig.class) // Testcontainers config
@ActiveProfiles("bootstrap-data")
public class RepositoryIT {

    @Autowired
    GameRepository gameRepository;

    @Autowired
    TournamentTeamRepository tournamentTeamRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TournamentRepository tournamentRepository;

    @Test
    void testFindGamesUserNotRegisteredForTeam() {
        Optional<Game> gameOptional1 = gameRepository.findById(1L);
        Optional<Game> gameOptional2 = gameRepository.findById(3L);
        Optional<User> userOptional = userRepository.findById(2L);
        Optional<Team> teamOptional = teamRepository.findById(1L);
        assertTrue(gameOptional1.isPresent());
        assertTrue(gameOptional2.isPresent());
        assertTrue(userOptional.isPresent());
        assertTrue(teamOptional.isPresent());
        Game game1 = gameOptional1.get();
        Game game2 = gameOptional2.get();
        User user = userOptional.get();
        Team team = teamOptional.get();

        List<Game> games = gameRepository.findGamesUserNotRegisteredForTeam(team, user);

        assertNotNull(games);
        assertFalse(games.contains(game1));
        assertTrue(games.contains(game2));
    }

    @Test
    void testFindTeamsOwnedByUserNotRegisteredInTournamentOrderByName() {
        Optional<Team> teamOptional1 = teamRepository.findById(1L);
        Optional<Team> teamOptional2 = teamRepository.findById(5L);
        Optional<Tournament> tournamentOptional = tournamentRepository.findById(1L);
        Optional<User> userOptional = userRepository.findById(2L);
        assertTrue(teamOptional1.isPresent());
        assertTrue(teamOptional2.isPresent());
        assertTrue(tournamentOptional.isPresent());
        assertTrue(userOptional.isPresent());
        Team team1 = teamOptional1.get();
        Team team2 = teamOptional2.get();
        Tournament tournament = tournamentOptional.get();
        User user = userOptional.get();

        List<Team> teams = teamRepository.findTeamsOwnedByUserNotRegisteredInTournamentOrderByName(tournament, user);

        assertNotNull(teams);
        assertFalse(teams.contains(team1));
        assertTrue(teams.contains(team2));
    }

    @Test
    void testRecountTournamentTeamScores() {
        Optional<Tournament> tournamentOptional = tournamentRepository.findById(1L);
        assertTrue(tournamentOptional.isPresent());
        Tournament tournament = tournamentOptional.get();

        Optional<TournamentTeam> tournamentTeamOptional1 = tournamentTeamRepository.findById(1L);
        assertTrue(tournamentTeamOptional1.isPresent());
        tournamentTeamOptional1.get().setScoreSum(0);
        tournamentTeamRepository.save(tournamentTeamOptional1.get());

        tournamentTeamRepository.recountTournamentTeamScores(tournament.getId());

        tournamentTeamOptional1 = tournamentTeamRepository.findById(1L);
        Optional<TournamentTeam> tournamentTeamOptional2 = tournamentTeamRepository.findById(2L);
        Optional<TournamentTeam> tournamentTeamOptional3 = tournamentTeamRepository.findById(3L);
        Optional<TournamentTeam> tournamentTeamOptional4 = tournamentTeamRepository.findById(4L);
        assertTrue(tournamentTeamOptional1.isPresent());
        assertTrue(tournamentTeamOptional2.isPresent());
        assertTrue(tournamentTeamOptional3.isPresent());
        assertTrue(tournamentTeamOptional4.isPresent());

        assertEquals(28, tournamentTeamOptional1.get().getScoreSum());
        assertEquals(30, tournamentTeamOptional2.get().getScoreSum());
        assertEquals(3, tournamentTeamOptional3.get().getScoreSum());
        assertEquals(0, tournamentTeamOptional4.get().getScoreSum());
    }

}
