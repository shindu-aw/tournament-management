package pjatk.s18617.tournamentmanagement.services;

import pjatk.s18617.tournamentmanagement.model.Announcement;
import pjatk.s18617.tournamentmanagement.dtos.AnnouncementCreationDto;
import pjatk.s18617.tournamentmanagement.model.Tournament;

public interface AnnouncementService {
    Announcement saveWithAuthorization(AnnouncementCreationDto dto, Tournament tournament, String username);

    void deleteWithAuthorization(Long announcementId, String username);
}
