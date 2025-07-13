package pjatk.s18617.tournamentmanagement.services;

import pjatk.s18617.tournamentmanagement.dtos.TeamUserCreationDto;
import pjatk.s18617.tournamentmanagement.model.Team;
import pjatk.s18617.tournamentmanagement.model.TeamUser;
import pjatk.s18617.tournamentmanagement.model.User;

public interface TeamUserService {
    void checkDeleteAuthorization(TeamUser teamUser, User user);

    void checkDeleteAuthorization(TeamUser teamUser, String username);

    TeamUser save(TeamUserCreationDto dto, Team team, String username);

    void deleteWithAuthorization(Long teamUserId, String username);
}
