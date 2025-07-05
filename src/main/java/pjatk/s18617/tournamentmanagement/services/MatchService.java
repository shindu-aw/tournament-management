package pjatk.s18617.tournamentmanagement.services;

import pjatk.s18617.tournamentmanagement.model.Match;

import java.util.Optional;

public interface MatchService {

    Optional<Match> findById(Long id);

    void deleteWithAuthorization(Match match, String username);

}
