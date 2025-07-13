package pjatk.s18617.tournamentmanagement.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pjatk.s18617.tournamentmanagement.model.AnnouncementCreationDto;
import pjatk.s18617.tournamentmanagement.model.Tournament;
import pjatk.s18617.tournamentmanagement.services.AnnouncementService;
import pjatk.s18617.tournamentmanagement.services.TournamentService;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class AnnouncementController {
    private final TournamentService tournamentService;
    private final AnnouncementService announcementService;

    @GetMapping("/tournament/{tournamentId}/announcement/new")
    public String showAnnouncementCreationForm(@PathVariable Long tournamentId, Model model, Principal principal) {
        Tournament tournament = tournamentService.getById(tournamentId).orElseThrow(NotFoundException::new);
        String currentUserName = principal.getName();

        tournamentService.checkAuthorization(tournament, currentUserName);

        model.addAttribute("tournament", tournament);
        model.addAttribute("announcementCreationDto", new AnnouncementCreationDto());

        return "tournament/tournament-announcement-add";
    }

    @PostMapping("/tournament/{tournamentId}/announcement/new")
    public String processAnnouncementCreationForm(@PathVariable Long tournamentId, Model model, Principal principal,
                                                  @Valid AnnouncementCreationDto announcementCreationDto,
                                                  BindingResult result) {
        Tournament tournament = tournamentService.getById(tournamentId).orElseThrow(NotFoundException::new);

        if (result.hasErrors()) {
            model.addAttribute("tournament", tournament);
            return "tournament/tournament-announcement-add";
        }

        String currentUserName = principal.getName();
        announcementService.saveWithAuthorization(announcementCreationDto, tournament, currentUserName);
        return "redirect:/tournament/" + tournamentId;
    }

    @PostMapping("/tournament/{tournamentId}/announcement/delete/{announcementId}")
    public String deleteAnnouncement(@PathVariable Long tournamentId, @PathVariable Long announcementId,
                                     Principal principal) {
        String currentUserName = principal.getName();
        announcementService.deleteWithAuthorization(announcementId, currentUserName);
        return "redirect:/tournament/" + tournamentId;
    }

}
