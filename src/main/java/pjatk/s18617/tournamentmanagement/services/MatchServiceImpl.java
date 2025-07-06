package pjatk.s18617.tournamentmanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pjatk.s18617.tournamentmanagement.controllers.NotFoundException;
import pjatk.s18617.tournamentmanagement.dtos.MatchCreationDto;
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

        // each TournamentTeam's scoreSum is updated in Match entity's JPA lifecycle methods TODO change to DB trigger

        matchRepository.delete(match);
    }

    @Transactional
    @Override
    public Match saveWithAuthorization(MatchCreationDto matchCreationDto, Tournament tournament, String username) {
        User user = userService.findByUsername(username).orElseThrow(NotFoundException::new);
        checkAuthorization(tournament, user);

        TournamentTeam tournamentTeam1 = tournamentTeamRepository.findById(matchCreationDto.getTournamentTeam1Id())
                .orElseThrow(NotFoundException::new);
        TournamentTeam tournamentTeam2 = tournamentTeamRepository.findById(matchCreationDto.getTournamentTeam2Id())
                .orElseThrow(NotFoundException::new);

        // each TournamentTeam's scoreSum is updated in Match entity's JPA lifecycle methods TODO change to DB trigger

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

}
