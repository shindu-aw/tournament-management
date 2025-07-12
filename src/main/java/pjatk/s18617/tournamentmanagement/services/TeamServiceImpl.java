package pjatk.s18617.tournamentmanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import pjatk.s18617.tournamentmanagement.controllers.NotFoundException;
import pjatk.s18617.tournamentmanagement.dtos.TeamCreationDto;
import pjatk.s18617.tournamentmanagement.dtos.TeamEditDto;
import pjatk.s18617.tournamentmanagement.model.Team;
import pjatk.s18617.tournamentmanagement.model.User;
import pjatk.s18617.tournamentmanagement.repositories.TeamRepository;
import pjatk.s18617.tournamentmanagement.utils.SecretCodeGenerator;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final UserService userService;

    @Override
    public void checkAuthorization(Team team, User user) {
        boolean userIsNotAdmin = !user.isAdmin();
        boolean userIsNotOwner = !user.equals(team.getUserOwner());
        boolean cannotManageTeam = userIsNotAdmin && userIsNotOwner;
        if (cannotManageTeam)
            throw new AccessDeniedException("Nie masz praw do zarządzania tą drużyną.");
    }

    @Override
    public void checkAuthorization(Team team, String username) {
        User user = userService.findByUsername(username).orElseThrow(NotFoundException::new);
        checkAuthorization(team, user);
    }

    @Override
    public Optional<Team> findById(Long teamId) {
        return teamRepository.findById(teamId);
    }

    @Override
    public void deleteWithAuthorization(Team team, String username) {
        checkAuthorization(team, username);
        teamRepository.delete(team);
    }

    @Override
    public Team save(TeamCreationDto dto, User userOwner) {
        Team team = Team.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .userOwner(userOwner)
                .secretCode(SecretCodeGenerator.generateSecretCode())
                .build();
        return teamRepository.save(team);
    }

    @Override
    public Team save(TeamCreationDto dto, String userOwnerUsername) {
        User user = userService.findByUsername(userOwnerUsername).orElseThrow(NotFoundException::new);
        return save(dto, user);
    }

    @Override
    public Team updateWithAuthorization(Team updatedTeam, TeamEditDto dto, String currentUserName) {
        checkAuthorization(updatedTeam, currentUserName);

        updatedTeam.setName(dto.getName());
        updatedTeam.setDescription(dto.getDescription());

        return teamRepository.save(updatedTeam);
    }

}
