package pjatk.s18617.tournamentmanagement.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pjatk.s18617.tournamentmanagement.dtos.TeamCreationDto;
import pjatk.s18617.tournamentmanagement.dtos.TeamEditDto;
import pjatk.s18617.tournamentmanagement.model.*;
import pjatk.s18617.tournamentmanagement.services.TeamService;
import pjatk.s18617.tournamentmanagement.services.UserService;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;
    private final UserService userService;

    @GetMapping("/team/{teamId}")
    public String showTeam(@PathVariable Long teamId, Model model) {
        Team viewedTeam = teamService.findById(teamId).orElseThrow(NotFoundException::new);

        // in-memory sort instead of DB call because they're small data sets
        List<TeamUser> userRegistrations = viewedTeam.getUserRegistrations().stream()
                .sorted(Comparator.comparing(TeamUser::getUserName)).toList();
        List<TournamentTeam> tournamentRegistrations = viewedTeam.getTournamentRegistrations().stream()
                .sorted(Comparator.comparing(TournamentTeam::getTournamentName)).toList();
        List<Link> links = viewedTeam.getLinks().stream()
                .sorted(Comparator.comparing(Link::getName)).toList();

        model.addAttribute("team", viewedTeam);
        model.addAttribute("userRegistrations", userRegistrations);
        model.addAttribute("tournamentRegistrations", tournamentRegistrations);
        model.addAttribute("links", links);
        return "team/team";
    }

    @PostMapping("/team/{teamId}/delete")
    public String deleteTeam(@PathVariable Long teamId, Principal principal, RedirectAttributes redirectAttributes) {
        Team teamToDelete = teamService.findById(teamId).orElseThrow(NotFoundException::new);
        String username = principal.getName();
        User userOwner = teamToDelete.getUserOwner();

        String message = "Drużyna '" + teamToDelete.getName() + "' usunięta.";
        redirectAttributes.addAttribute("message", message);

        teamService.deleteWithAuthorization(teamToDelete, username);
        return "redirect:/user/" + userOwner.getId();
    }

    @GetMapping("/team/new")
    public String showTeamCreationForm(Model model) {
        model.addAttribute("teamCreationDto", new TeamCreationDto());
        return "team/team-add";
    }

    @PostMapping("/team/new")
    public String processTeamCreationForm(@Valid TeamCreationDto teamCreationDto, BindingResult result,
                                          Principal principal) {
        if (result.hasErrors())
            return "team/team-add";

        String currentUserName = principal.getName();
        Team newTeam = teamService.save(teamCreationDto, currentUserName);
        return "redirect:/team/" + newTeam.getId();
    }

    @GetMapping("/team/{teamId}/edit")
    public String showTeamEditForm(@PathVariable Long teamId, Model model, Principal principal) {
        Team editedTeam = teamService.findById(teamId).orElseThrow(NotFoundException::new);

        String username = principal.getName();
        teamService.checkAuthorization(editedTeam, username);

        model.addAttribute("teamEditDto", new TeamEditDto(
                editedTeam.getId(), editedTeam.getName(), editedTeam.getDescription()
        ));
        model.addAttribute("team", editedTeam);

        return "team/team-edit";
    }

    @PostMapping("/team/{teamId}/edit")
    public String processTeamEditForm(@PathVariable Long teamId, Principal principal, Model model,
                                      @Valid TeamEditDto teamEditDto, BindingResult result) {
        Team editedTeam = teamService.findById(teamId).orElseThrow(NotFoundException::new);

        if (result.hasErrors()) {
            model.addAttribute("team", editedTeam);
            return "team/team-edit";
        }

        String currentUserName = principal.getName();
        teamService.updateWithAuthorization(editedTeam, teamEditDto, currentUserName);

        return "redirect:/team/" + editedTeam.getId();
    }

    @PostMapping("/team/{teamId}/regenerate-secret-code")
    public String regenerateSecretCode(@PathVariable Long teamId, Principal principal) {
        String currentUserName = principal.getName();

        teamService.regenerateSecretCodeWithAuthorization(teamId, currentUserName);

        return "redirect:/team/" + teamId;
    }

}
