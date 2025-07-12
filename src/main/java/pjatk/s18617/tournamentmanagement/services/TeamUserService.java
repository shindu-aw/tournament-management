package pjatk.s18617.tournamentmanagement.services;

import pjatk.s18617.tournamentmanagement.dtos.TeamUserCreationDto;
import pjatk.s18617.tournamentmanagement.model.Team;
import pjatk.s18617.tournamentmanagement.model.TeamUser;

public interface TeamUserService {
    TeamUser save(TeamUserCreationDto dto, Team team, String username);
}
