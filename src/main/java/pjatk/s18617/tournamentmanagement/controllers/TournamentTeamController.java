package pjatk.s18617.tournamentmanagement.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pjatk.s18617.tournamentmanagement.dtos.TournamentTeamCreationDto;
import pjatk.s18617.tournamentmanagement.model.Team;
import pjatk.s18617.tournamentmanagement.model.Tournament;
import pjatk.s18617.tournamentmanagement.model.User;
import pjatk.s18617.tournamentmanagement.services.TeamService;
import pjatk.s18617.tournamentmanagement.services.TournamentService;
import pjatk.s18617.tournamentmanagement.services.TournamentTeamService;
import pjatk.s18617.tournamentmanagement.services.UserService;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class TournamentTeamController {

    private final TournamentService tournamentService;
    private final TournamentTeamService tournamentTeamService;
    private final UserService userService;
    private final TeamService teamService;

    @PostMapping("/tournament/{tournamentId}/team/remove/{tournamentTeamId}")
    public String removeTeamFromTournament(@PathVariable Long tournamentId, @PathVariable Long tournamentTeamId,
                                           Principal principal) {
        String currentUsername = principal.getName();
        tournamentTeamService.deleteWithAuthorization(tournamentTeamId, currentUsername);
        return "redirect:/tournament/" + tournamentId;
    }

    @PostMapping("/team/{teamId}/tournament/leave/{tournamentTeamId}")
    public String leaveTournament(@PathVariable Long teamId, @PathVariable Long tournamentTeamId, Principal principal) {
        String currentUsername = principal.getName();
        tournamentTeamService.deleteWithAuthorization(tournamentTeamId, currentUsername);
        return "redirect:/team/" + teamId;
    }

    @GetMapping("/tournament/{tournamentId}/team/add")
    public String showTeamAdditionForm(@PathVariable Long tournamentId, Model model, Principal principal) {
        Tournament tournament = tournamentService.getById(tournamentId).orElseThrow(NotFoundException::new);
        tournamentService.throwBadRequestIfFinished(tournament);

        String username = principal.getName();
        User user = userService.findByUsername(username).orElseThrow(NotFoundException::new);

        // filters out already registered teams and sorts them alphabetically by name
        List<Team> teamsOwned = teamService.findTeamsOwnedByUserNotRegisteredInTournamentOrderByName(tournament, user);

        model.addAttribute("tournamentTeamCreationDto", new TournamentTeamCreationDto());
        model.addAttribute("tournament", tournament);
        model.addAttribute("teamsOwned", teamsOwned);

        return "tournament/tournament-team-add";
    }

    @PostMapping("/tournament/{tournamentId}/team/add")
    public String processTeamAdditionForm(@PathVariable Long tournamentId, Model model, Principal principal,
                                          @Valid TournamentTeamCreationDto tournamentTeamCreationDto,
                                          BindingResult result) {
        Tournament tournament = tournamentService.getById(tournamentId).orElseThrow(NotFoundException::new);
        String username = principal.getName();
        User user = userService.findByUsername(username).orElseThrow(NotFoundException::new);

        if (!tournamentTeamCreationDto.getSecretCode().equals(tournament.getJoinSecretCode()))
            result.rejectValue("secretCode", "error.secretCode", "zły kod");

        if (result.hasErrors()) {
            // filters out already registered teams and sorts them alphabetically by name
            List<Team> teamsOwned = teamService.findTeamsOwnedByUserNotRegisteredInTournamentOrderByName(
                    tournament, user
            );
            model.addAttribute("tournament", tournament);
            model.addAttribute("teamsOwned", teamsOwned);
            return "tournament/tournament-team-add";
        }

        tournamentTeamService.saveWithAuthorization(tournamentTeamCreationDto, tournament, user);
        return "redirect:/tournament/" + tournamentId;
    }

}
