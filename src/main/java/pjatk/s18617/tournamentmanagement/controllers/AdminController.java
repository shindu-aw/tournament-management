package pjatk.s18617.tournamentmanagement.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import pjatk.s18617.tournamentmanagement.dtos.DateDto;
import pjatk.s18617.tournamentmanagement.services.TournamentService;
import pjatk.s18617.tournamentmanagement.services.UserService;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class AdminController {
    private final TournamentService tournamentService;
    private final UserService userService;

    @GetMapping("/admin/delete-old-tournaments")
    public String showDeleteOldTournamentsForm(Model model, Principal principal) {
        String currentUserName = principal.getName();
        userService.checkAdminAuthorization(currentUserName);

        model.addAttribute("dateDto", new DateDto());
        return "delete-old-tournaments-form";
    }

    @PostMapping("/admin/delete-old-tournaments")
    public String processDeleteOldTournamentsForm(Model model, Principal principal,
                                                  @Valid DateDto dateDto, BindingResult result) {
        if (result.hasErrors())
            return "delete-old-tournaments-form";

        String currentUserName = principal.getName();

        int tournamentsDeleted = tournamentService.deleteCompletedTournamentsOlderThanWithAdminAuthorization(
                dateDto.getDate(), currentUserName
        );

        model.addAttribute("tournamentsDeleted", tournamentsDeleted);
        return "delete-old-tournaments-form";
    }

}
