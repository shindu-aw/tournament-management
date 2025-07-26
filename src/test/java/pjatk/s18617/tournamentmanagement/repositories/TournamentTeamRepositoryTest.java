package pjatk.s18617.tournamentmanagement.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import pjatk.s18617.tournamentmanagement.model.Team;
import pjatk.s18617.tournamentmanagement.model.Tournament;
import pjatk.s18617.tournamentmanagement.model.TournamentTeam;
import pjatk.s18617.tournamentmanagement.model.User;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("h2-test")
@DataJpaTest
class TournamentTeamRepositoryTest {

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
                .secretCode("12345678")
                .build();
        Team team2 = Team.builder()
                .name("team2")
                .description("description2")
                .userOwner(user)
                .secretCode("12345678")
                .build();
        teamRepository.saveAll(Arrays.asList(team, team2));


        // create
        TournamentTeam tournamentTeam = TournamentTeam.builder()
                .tournament(tournament)
                .team(team)
                .build();
        TournamentTeam saved = tournamentTeamRepository.save(tournamentTeam);
        assertNotNull(saved);

        // read
        Optional<TournamentTeam> found = tournamentTeamRepository.findById(saved.getId());
        assertTrue(found.isPresent());

        // update
        saved.setTeam(team2);
        TournamentTeam updated =  tournamentTeamRepository.save(saved);
        assertEquals(team2, updated.getTeam());

        // delete
        tournamentTeamRepository.delete(updated);
        Optional<TournamentTeam> deleted = tournamentTeamRepository.findById(updated.getId());
        assertFalse(deleted.isPresent());
    }

}