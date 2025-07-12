package pjatk.s18617.tournamentmanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import pjatk.s18617.tournamentmanagement.controllers.NotFoundException;
import pjatk.s18617.tournamentmanagement.model.Team;
import pjatk.s18617.tournamentmanagement.model.User;
import pjatk.s18617.tournamentmanagement.repositories.TeamRepository;

import java.util.List;
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
        boolean cannotManageLocation = userIsNotAdmin && userIsNotOwner;
        if (cannotManageLocation)
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

}
