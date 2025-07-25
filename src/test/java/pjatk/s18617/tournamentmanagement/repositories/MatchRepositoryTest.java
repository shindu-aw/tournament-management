package pjatk.s18617.tournamentmanagement.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import pjatk.s18617.tournamentmanagement.model.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("h2-test")
@DataJpaTest
class MatchRepositoryTest {

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TournamentTeamRepository tournamentTeamRepository;

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
        Team team = Team.builder()
                .name("team")
                .description("description")
                .userOwner(user)
                .build();
        Team team2 = Team.builder()
                .name("team2")
                .description("description2")
                .userOwner(user)
                .build();
        teamRepository.saveAll(Arrays.asList(team, team2));
        TournamentTeam tournamentTeam = TournamentTeam.builder()
                .tournament(tournament)
                .team(team)
                .build();
        TournamentTeam tournamentTeam2 = TournamentTeam.builder()
                .tournament(tournament)
                .team(team2)
                .build();
        tournamentTeamRepository.saveAll(Arrays.asList(tournamentTeam, tournamentTeam2));


        // create
        Match match = Match.builder()
                .tournament(tournament)
                .tournamentTeam1(tournamentTeam)
                .tournamentTeam2(tournamentTeam2)
                .team1Score(1)
                .team2Score(2)
                .build();
        Match saved = matchRepository.save(match);
        assertNotNull(saved);

        // read
        Optional<Match> found = matchRepository.findById(saved.getId());
        assertTrue(found.isPresent());

        // update
        saved.setTeam1Score(3);
        Match updated = matchRepository.save(saved);
        assertEquals(3, updated.getTeam1Score());

        // delete
        matchRepository.deleteById(saved.getId());
        Optional<Match> deleted = matchRepository.findById(saved.getId());
        assertFalse(deleted.isPresent());
    }

}