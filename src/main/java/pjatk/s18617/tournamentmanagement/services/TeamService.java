package pjatk.s18617.tournamentmanagement.services;

import pjatk.s18617.tournamentmanagement.model.Team;
import pjatk.s18617.tournamentmanagement.model.User;

import java.util.Optional;

public interface TeamService {

    void checkAuthorization(Team team, User user);

    void checkAuthorization(Team team, String username);

    Optional<Team> findById(Long teamId);

    void deleteWithAuthorization(Team team, String username);

}
