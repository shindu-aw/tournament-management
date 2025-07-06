package pjatk.s18617.tournamentmanagement.services;

import pjatk.s18617.tournamentmanagement.dtos.MatchCreationDto;
import pjatk.s18617.tournamentmanagement.model.Match;
import pjatk.s18617.tournamentmanagement.model.Tournament;
import pjatk.s18617.tournamentmanagement.model.User;

import java.util.Optional;

public interface MatchService {

    void checkAuthorization(Tournament tournament, String username);

    void checkAuthorization(Tournament tournament, User user);

    Optional<Match> findById(Long id);

    void deleteWithAuthorization(Match match, String username);

    Match saveWithAuthorization(MatchCreationDto matchCreationDto, Tournament tournament, String username);

}
