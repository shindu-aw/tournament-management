package pjatk.s18617.tournamentmanagement.services;

import pjatk.s18617.tournamentmanagement.dtos.LocationCreationDto;
import pjatk.s18617.tournamentmanagement.model.Location;
import pjatk.s18617.tournamentmanagement.model.Tournament;
import pjatk.s18617.tournamentmanagement.model.User;

public interface LocationService {

    void checkAuthorization(Tournament tournament, String username);

    void checkAuthorization(Tournament tournament, User user);

    Location saveWithAuthorization(LocationCreationDto locationCreationDto, Tournament tournament, String username);

    void deleteWithAuthorization(Tournament tournament, String username);

}
