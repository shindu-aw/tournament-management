package pjatk.s18617.tournamentmanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pjatk.s18617.tournamentmanagement.controllers.NotFoundException;
import pjatk.s18617.tournamentmanagement.model.Announcement;
import pjatk.s18617.tournamentmanagement.model.AnnouncementCreationDto;
import pjatk.s18617.tournamentmanagement.model.Tournament;
import pjatk.s18617.tournamentmanagement.repositories.AnnouncementRepository;

@RequiredArgsConstructor
@Service
public class AnnouncementServiceImpl implements AnnouncementService {

    private final TournamentService tournamentService;
    private final AnnouncementRepository announcementRepository;

    @Override
    public Announcement saveWithAuthorization(AnnouncementCreationDto dto, Tournament tournament, String username) {
        tournamentService.checkAuthorization(tournament, username);

        Announcement announcement = Announcement.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .tournament(tournament)
                .build();

        return announcementRepository.save(announcement);
    }

    @Override
    public void deleteWithAuthorization(Long announcementId, String username) {
        Announcement announcement = announcementRepository.findById(announcementId).orElseThrow(NotFoundException::new);
        tournamentService.checkAuthorization(announcement.getTournament(), username);
        announcementRepository.delete(announcement);
    }

}
