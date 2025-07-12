package pjatk.s18617.tournamentmanagement.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pjatk.s18617.tournamentmanagement.dtos.LinkCreationDto;
import pjatk.s18617.tournamentmanagement.model.Team;
import pjatk.s18617.tournamentmanagement.services.LinkService;
import pjatk.s18617.tournamentmanagement.services.TeamService;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class LinkController {

    private final TeamService teamService;
    private final LinkService linkService;

    @GetMapping("/team/{teamId}/new/link")
    public String showLinkCreationForm(@PathVariable Long teamId, Model model, Principal principal) {
        Team team = teamService.findById(teamId).orElseThrow(NotFoundException::new);
        String currentUserName = principal.getName();

        teamService.checkAuthorization(team, currentUserName);

        model.addAttribute("team", team);
        model.addAttribute("linkCreationDto", new LinkCreationDto());

        return "team/team-link-add";
    }

    @PostMapping("/team/{teamId}/new/link")
    public String processLinkCreationForm(@PathVariable Long teamId, Model model, Principal principal,
                                          @Valid LinkCreationDto linkCreationDto,  BindingResult result) {
        Team team = teamService.findById(teamId).orElseThrow(NotFoundException::new);

        if (result.hasErrors()) {
            model.addAttribute("team", team);
            return "team/team-link-add";
        }

        String username = principal.getName();
        linkService.saveWithAuthorization(linkCreationDto, team, username);
        return "redirect:/team/" + teamId;
    }

}
