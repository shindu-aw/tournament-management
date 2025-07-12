package pjatk.s18617.tournamentmanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pjatk.s18617.tournamentmanagement.controllers.NotFoundException;
import pjatk.s18617.tournamentmanagement.dtos.TournamentTeamCreationDto;
import pjatk.s18617.tournamentmanagement.model.Team;
import pjatk.s18617.tournamentmanagement.model.Tournament;
import pjatk.s18617.tournamentmanagement.model.TournamentTeam;
import pjatk.s18617.tournamentmanagement.model.User;
import pjatk.s18617.tournamentmanagement.repositories.TeamRepository;
import pjatk.s18617.tournamentmanagement.repositories.TournamentTeamRepository;

@RequiredArgsConstructor
@Service
public class TournamentTeamServiceImpl implements TournamentTeamService {

    private final UserService userService;
    private final TournamentService tournamentService;
    private final TournamentTeamRepository tournamentTeamRepository;
    private final TeamRepository teamRepository;
    private final TeamService teamService;
    private final MatchService matchService;

    @Transactional
    @Override
    public void deleteWithAuthorization(Long tournamentTeamId, String username) {
        TournamentTeam tournamentTeam = tournamentTeamRepository.findById(tournamentTeamId)
                .orElseThrow(NotFoundException::new);
        tournamentService.checkAuthorization(tournamentTeam.getTournament(), username);

        // all matches connected to this TournamentTeam entity are automatically deleted thanks to CascadeType.DELETE

        tournamentTeamRepository.delete(tournamentTeam);
    }

    @Override
    public TournamentTeam saveWithAuthorization(TournamentTeamCreationDto dto, Tournament tournament, User user) {
        if (!dto.getSecretCode().equals(tournament.getJoinSecretCode()))
            throw new AccessDeniedException("Zły tajny kod do dodania drużyny.");

        Team team = teamRepository.findById(dto.getTeamId()).orElseThrow(NotFoundException::new);

        teamService.checkAuthorization(team, user);

        TournamentTeam tournamentTeam = TournamentTeam.builder()
                .team(team)
                .tournament(tournament)
                .build();

        return tournamentTeamRepository.save(tournamentTeam);
    }

}
