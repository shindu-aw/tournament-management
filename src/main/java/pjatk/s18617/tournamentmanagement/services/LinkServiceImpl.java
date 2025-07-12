package pjatk.s18617.tournamentmanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pjatk.s18617.tournamentmanagement.controllers.NotFoundException;
import pjatk.s18617.tournamentmanagement.dtos.LinkCreationDto;
import pjatk.s18617.tournamentmanagement.model.Link;
import pjatk.s18617.tournamentmanagement.model.Team;
import pjatk.s18617.tournamentmanagement.repositories.LinkRepository;

@RequiredArgsConstructor
@Service
public class LinkServiceImpl implements LinkService {

    private final UserService userService;
    private final TeamService teamService;
    private final LinkRepository linkRepository;

    @Override
    public Link saveWithAuthorization(LinkCreationDto dto, Team team, String currentUserName) {
        teamService.checkAuthorization(team, currentUserName);

        Link link = Link.builder()
                .name(dto.getName())
                .url(dto.getUrl())
                .team(team)
                .build();

        return linkRepository.save(link);
    }

    @Override
    public void deleteWithAuthorization(Long linkId, String currentUserName) {
        Link linkToDelete = linkRepository.findById(linkId).orElseThrow(NotFoundException::new);
        teamService.checkAuthorization(linkToDelete.getTeam(), currentUserName);
        linkRepository.delete(linkToDelete);
    }

}
