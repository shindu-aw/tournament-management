package pjatk.s18617.tournamentmanagement.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pjatk.s18617.tournamentmanagement.dtos.SecretCodeDto;
import pjatk.s18617.tournamentmanagement.model.Tournament;
import pjatk.s18617.tournamentmanagement.services.TournamentService;
import pjatk.s18617.tournamentmanagement.services.UserService;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class TournamentModeratorController {

    private final TournamentService tournamentService;
    private final UserService userService;

    @PostMapping("/tournament/{tournamentId}/moderator/remove/{userId}")
    public String removeModerator(@PathVariable Long tournamentId, @PathVariable Long userId, Principal principal) {
        String currentUserName = principal.getName();
        tournamentService.removeUserModeratorWithAuthorization(tournamentId, userId, currentUserName);
        return "redirect:/tournament/" + tournamentId;
    }

    @GetMapping("/tournament/{tournamentId}/moderator/apply")
    public String showModeratorApplicationForm(@PathVariable Long tournamentId, Model model) {
        Tournament tournament = tournamentService.getById(tournamentId).orElseThrow(NotFoundException::new);

        model.addAttribute("tournament", tournament);
        model.addAttribute("secretCodeDto", new SecretCodeDto());

        return "tournament/tournament-moderator-apply";
    }

    @PostMapping("/tournament/{tournamentId}/moderator/apply")
    public String processModeratorApplicationForm(@PathVariable Long tournamentId, Model model, Principal principal,
                                                  @Valid SecretCodeDto secretCodeDto, BindingResult result) {
        Tournament tournament = tournamentService.getById(tournamentId).orElseThrow(NotFoundException::new);
        String username = principal.getName();

        if (!secretCodeDto.getSecretCode().equals(tournament.getManageSecretCode()))
            result.rejectValue("secretCode", "error.secretCode", "zły kod");

        if (result.hasErrors()) {
            model.addAttribute("tournament", tournament);
            return "tournament/tournament-moderator-apply";
        }

        tournamentService.addUserModerator(tournament, username);

        return "redirect:/tournament/" + tournamentId;
    }

}
