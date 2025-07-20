package pjatk.s18617.tournamentmanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pjatk.s18617.tournamentmanagement.controllers.NotFoundException;
import pjatk.s18617.tournamentmanagement.dtos.MatchCreationDto;
import pjatk.s18617.tournamentmanagement.dtos.MatchEditDto;
import pjatk.s18617.tournamentmanagement.model.Match;
import pjatk.s18617.tournamentmanagement.model.Tournament;
import pjatk.s18617.tournamentmanagement.model.TournamentTeam;
import pjatk.s18617.tournamentmanagement.model.User;
import pjatk.s18617.tournamentmanagement.repositories.MatchRepository;
import pjatk.s18617.tournamentmanagement.repositories.TournamentTeamRepository;

import java.util.Arrays;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    private final UserService userService;
    private final TournamentTeamRepository tournamentTeamRepository;
    private final TournamentService tournamentService;

    @Override
    public void checkAuthorization(Tournament tournament, String username) {
        User user = userService.findByUsername(username).orElseThrow(NotFoundException::new);
        checkAuthorization(tournament, user);
    }

    @Override
    public void checkAuthorization(Tournament tournament, User user) {
        boolean userIsNotAdmin = !user.isAdmin();
        boolean userIsNotOwner = !user.equals(tournament.getUserOwner());
        boolean userIsNotManager = !tournament.isManagedByUser(user.getUsername());
        boolean cannotManageMatches = userIsNotAdmin && userIsNotOwner && userIsNotManager;
        if (cannotManageMatches)
            throw new AccessDeniedException("Nie masz praw do zarządzania meczami w tym turnieju.");
    }

    @Override
    public Optional<Match> findById(Long id) {
        return matchRepository.findById(id);
    }

    @Override
    public void deleteWithAuthorization(Match match, String username) {
        User user = userService.findByUsername(username).orElseThrow(NotFoundException::new);
        checkAuthorization(match.getTournament(), user);
        tournamentService.throwBadRequestIfFinished(match.getTournament());

        // TournamentTeam score sums are updated through database triggers (added in migration V2)

        matchRepository.delete(match);
    }

    @Transactional
    @Override
    public Match saveWithAuthorization(MatchCreationDto matchCreationDto, Tournament tournament, String username) {
        User user = userService.findByUsername(username).orElseThrow(NotFoundException::new);
        checkAuthorization(tournament, user);
        tournamentService.throwBadRequestIfFinished(tournament);

        TournamentTeam tournamentTeam1 = tournamentTeamRepository.findById(matchCreationDto.getTournamentTeam1Id())
                .orElseThrow(NotFoundException::new);
        TournamentTeam tournamentTeam2 = tournamentTeamRepository.findById(matchCreationDto.getTournamentTeam2Id())
                .orElseThrow(NotFoundException::new);

        // TournamentTeam score sums are updated through database triggers (added in migration V2)

        Match match = Match.builder()
                .tournament(tournament)
                .tournamentTeam1(tournamentTeam1)
                .tournamentTeam2(tournamentTeam2)
                .date(matchCreationDto.getDate())
                .team1Score(matchCreationDto.getTeam1Score())
                .team2Score(matchCreationDto.getTeam2Score())
                .build();

        return matchRepository.save(match);
    }

    @Transactional
    public Match updateWithAuthorization(Match match, MatchEditDto matchEditDto, String username) {
        User user = userService.findByUsername(username).orElseThrow(NotFoundException::new);
        checkAuthorization(match.getTournament(), user);
        tournamentService.throwBadRequestIfFinished(match.getTournament());

        TournamentTeam newTournamentTeam1 = tournamentTeamRepository.findById(matchEditDto.getTournamentTeam1Id())
                .orElseThrow(NotFoundException::new);
        TournamentTeam newTournamentTeam2 = tournamentTeamRepository.findById(matchEditDto.getTournamentTeam2Id())
                .orElseThrow(NotFoundException::new);

        // TournamentTeam score sums are updated through database triggers (added in migration V2)

        // update match
        match.setTeam1Score(matchEditDto.getTeam1Score());
        match.setTeam2Score(matchEditDto.getTeam2Score());
        match.setDate(matchEditDto.getDate());
        match.setTournamentTeam1(newTournamentTeam1);
        match.setTournamentTeam2(newTournamentTeam2);
        return matchRepository.save(match);
    }

}
