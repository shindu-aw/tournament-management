package pjatk.s18617.tournamentmanagement.services;

import pjatk.s18617.tournamentmanagement.dtos.TournamentTeamCreationDto;
import pjatk.s18617.tournamentmanagement.model.Tournament;
import pjatk.s18617.tournamentmanagement.model.TournamentTeam;
import pjatk.s18617.tournamentmanagement.model.User;

public interface TournamentTeamService {

    void checkDeleteAuthorization(TournamentTeam tournamentTeam, User user);

    void checkDeleteAuthorization(TournamentTeam tournamentTeam, String username);

    void deleteWithAuthorization(Long tournamentTeamId, String username);

    TournamentTeam saveWithAuthorization(TournamentTeamCreationDto dto, Tournament tournament, User user);

}
