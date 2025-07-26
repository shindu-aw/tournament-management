package pjatk.s18617.tournamentmanagement.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import pjatk.s18617.tournamentmanagement.MyTestContainerConfig;
import pjatk.s18617.tournamentmanagement.model.*;
import pjatk.s18617.tournamentmanagement.repositories.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(MyTestContainerConfig.class) // Testcontainers config
@ActiveProfiles("bootstrap-data")
public class MatchScoreSumTriggerIT {

    @Autowired
    TournamentTeamRepository tournamentTeamRepository;

    @Autowired
    MatchRepository matchRepository;

    @Test
    void testMatchScoreSumOnInsertTrigger() {
        // this test takes the advantage of bootstrap data, so no new insertions are necessary for the setup
        TournamentTeam team1 = tournamentTeamRepository.findById(9L)
                .orElseThrow(() -> new AssertionError("TournamentTeam with ID 9 not found"));
        TournamentTeam team2 = tournamentTeamRepository.findById(10L)
                .orElseThrow(() -> new AssertionError("TournamentTeam with ID 10 not found"));

        assertEquals(12, team1.getScoreSum(), "Initial score sum for Team 1 should be correct");
        assertEquals(30, team2.getScoreSum(), "Initial score sum for Team 2 should be correct");
    }

    @Test
    void testMatchScoreSumOnUpdateTrigger() {
        // 1. arrange
        TournamentTeam team1 = tournamentTeamRepository.findById(5L)
                .orElseThrow(() -> new AssertionError("TournamentTeam with ID 5 not found"));
        TournamentTeam team2 = tournamentTeamRepository.findById(6L)
                .orElseThrow(() -> new AssertionError("TournamentTeam with ID 6 not found"));

        Match match = matchRepository.findById(4L)
                .orElseThrow(() -> new AssertionError("Match with ID 4 not found"));

        // 2. act
        match.setTournamentTeam1(team2); // swap teams
        match.setTournamentTeam2(team1);
        match.setTeam1Score(10);
        match.setTeam2Score(5);
        matchRepository.save(match); // should trigger on-update logic

        // 3. assert
        TournamentTeam updatedTeam1 = tournamentTeamRepository.findById(5L)
                .orElseThrow(() -> new AssertionError("TournamentTeam with ID 5 not found after update"));
        TournamentTeam updatedTeam2 = tournamentTeamRepository.findById(6L)
                .orElseThrow(() -> new AssertionError("TournamentTeam with ID 6 not found after update"));

        assertEquals(5, updatedTeam1.getScoreSum(), "Score sum for Team 1 should be updated after match update");
        assertEquals(10, updatedTeam2.getScoreSum(), "Score sum for Team 2 should be updated after match update");
    }

    @Test
    void testMatchScoreSumOnDeleteTrigger() {
        // 1. arrange
        tournamentTeamRepository.findById(7L)
                .orElseThrow(() -> new AssertionError("TournamentTeam with ID 7 not found"));
        tournamentTeamRepository.findById(8L)
                .orElseThrow(() -> new AssertionError("TournamentTeam with ID 8 not found"));

        Match matchToDelete = matchRepository.findById(5L)
                .orElseThrow(() -> new AssertionError("Match with ID 5 not found for deletion test"));

        // 2. act
        matchRepository.delete(matchToDelete); // should trigger on-delete logic

        // 3. assert
        TournamentTeam updatedTeam1 = tournamentTeamRepository.findById(7L)
                .orElseThrow(() -> new AssertionError("TournamentTeam with ID 7 not found after delete"));
        TournamentTeam updatedTeam2 = tournamentTeamRepository.findById(8L)
                .orElseThrow(() -> new AssertionError("TournamentTeam with ID 8 not found after delete"));

        assertEquals(0, updatedTeam1.getScoreSum(), "Score sum for Team 1 should be updated after match deletion");
        assertEquals(0, updatedTeam2.getScoreSum(), "Score sum for Team 2 should be updated after match deletion");
    }

}
