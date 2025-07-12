package pjatk.s18617.tournamentmanagement.services;

import pjatk.s18617.tournamentmanagement.model.Team;

import java.util.Optional;

public interface TeamService {

    Optional<Team> findById(Long teamId);

}
