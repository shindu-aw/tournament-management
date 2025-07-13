package pjatk.s18617.tournamentmanagement.services;

import pjatk.s18617.tournamentmanagement.dtos.TeamCreationDto;
import pjatk.s18617.tournamentmanagement.dtos.TeamEditDto;
import pjatk.s18617.tournamentmanagement.model.Team;
import pjatk.s18617.tournamentmanagement.model.User;

import java.util.Optional;

public interface TeamService {

    void checkAuthorization(Team team, User user);

    void checkAuthorization(Team team, String username);

    Optional<Team> findById(Long teamId);

    void deleteWithAuthorization(Team team, String username);

    Team save(TeamCreationDto dto, User userOwner);

    Team save(TeamCreationDto dto, String currentUserName);

    Team updateWithAuthorization(Team updatedTeam, TeamEditDto dto, String currentUserName);

    Team regenerateSecretCodeWithAuthorization(Long teamId, String currentUserName);
}
