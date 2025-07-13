package pjatk.s18617.tournamentmanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pjatk.s18617.tournamentmanagement.controllers.NotFoundException;
import pjatk.s18617.tournamentmanagement.dtos.TeamUserCreationDto;
import pjatk.s18617.tournamentmanagement.model.Game;
import pjatk.s18617.tournamentmanagement.model.Team;
import pjatk.s18617.tournamentmanagement.model.TeamUser;
import pjatk.s18617.tournamentmanagement.model.User;
import pjatk.s18617.tournamentmanagement.repositories.TeamUserRepository;

@RequiredArgsConstructor
@Service
public class TeamUserServiceImpl implements TeamUserService {

    private final TeamUserRepository teamUserRepository;
    private final UserService userService;
    private final GameService gameService;
    private final TeamService teamService;

    @Override
    public void checkDeleteAuthorization(TeamUser teamUser, User user) {
        boolean userIsNotAdmin = !user.isAdmin();
        boolean userIsNotTeamOwner = !user.equals(teamUser.getTeam().getUserOwner());
        boolean userIsNotMember = !user.equals(teamUser.getUser());
        boolean cannotDeleteMembership = userIsNotAdmin && userIsNotTeamOwner && userIsNotMember;
        if (cannotDeleteMembership)
            throw new AccessDeniedException("Nie masz praw do usunięcia tego członkostwa.");
    }

    @Override
    public void checkDeleteAuthorization(TeamUser teamUser, String username) {
        User user = userService.findByUsername(username).orElseThrow(NotFoundException::new);
        checkDeleteAuthorization(teamUser, user);
    }

    @Override
    public TeamUser save(TeamUserCreationDto dto, Team team, String username) {
        if (!dto.getSecretCode().equals(team.getSecretCode()))
            throw new AccessDeniedException("Zły tajny kod do dołączenia do drużyny.");

        User user = userService.findByUsername(username).orElseThrow(NotFoundException::new);
        Game game = gameService.getById(dto.getGameId()).orElseThrow(NotFoundException::new);

        // checks whether the user is already a member of this team assigned to this game
        if (!team.doesNotHaveUserRegisteredOnGame(user, game))
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Już jesteś członkiem tej drużyny przypisanym do tej gry."
            );

        TeamUser teamUser = TeamUser.builder()
                .team(team)
                .user(user)
                .game(game)
                .build();

        return teamUserRepository.save(teamUser);
    }

    @Override
    public void deleteWithAuthorization(Long teamUserId, String username) {
        TeamUser teamUser = teamUserRepository.findById(teamUserId).orElseThrow(NotFoundException::new);
        checkDeleteAuthorization(teamUser, username);

        teamUserRepository.delete(teamUser);
    }

}
