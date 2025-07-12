package pjatk.s18617.tournamentmanagement.services;

import pjatk.s18617.tournamentmanagement.dtos.LinkCreationDto;
import pjatk.s18617.tournamentmanagement.model.Link;
import pjatk.s18617.tournamentmanagement.model.Team;

public interface LinkService {
    Link saveWithAuthorization(LinkCreationDto dto, Team team, String currentUserName);

    void deleteWithAuthorization(Long linkId, String currentUserName);
}
