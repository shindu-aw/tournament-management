package pjatk.s18617.tournamentmanagement.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pjatk.s18617.tournamentmanagement.dtos.TeamUserCreationDto;
import pjatk.s18617.tournamentmanagement.model.Game;
import pjatk.s18617.tournamentmanagement.model.Team;
import pjatk.s18617.tournamentmanagement.model.User;
import pjatk.s18617.tournamentmanagement.services.GameService;
import pjatk.s18617.tournamentmanagement.services.TeamService;
import pjatk.s18617.tournamentmanagement.services.TeamUserService;
import pjatk.s18617.tournamentmanagement.services.UserService;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class TeamUserController {

    private final TeamService teamService;
    private final GameService gameService;
    private final TeamUserService teamUserService;
    private final UserService userService;

    @GetMapping("/team/{teamId}/member/apply")
    public String showMemberApplicationForm(@PathVariable Long teamId, Model model, Principal principal) {
        Team team = teamService.findById(teamId).orElseThrow(NotFoundException::new);
        String username = principal.getName();
        User user = userService.findByUsername(username).orElseThrow(NotFoundException::new);

        List<Game> games = gameService.getGamesUserNotRegisteredForTeam(team, user);

        model.addAttribute("team", team);
        model.addAttribute("games", games);
        model.addAttribute("teamUserCreationDto", new TeamUserCreationDto());

        return "team/team-member-apply";
    }

    @PostMapping("/team/{teamId}/member/apply")
    public String processMemberApplicationForm(@PathVariable Long teamId, Model model, Principal principal,
                                               @Valid TeamUserCreationDto teamUserCreationDto, BindingResult result) {
        Team team = teamService.findById(teamId).orElseThrow(NotFoundException::new);
        String username = principal.getName();
        User user = userService.findByUsername(username).orElseThrow(NotFoundException::new);

        if (!teamUserCreationDto.getSecretCode().equals(team.getSecretCode()))
            result.rejectValue("secretCode", "error.secretCode", "zły kod");

        if (result.hasErrors()) {
            List<Game> games = gameService.getGamesUserNotRegisteredForTeam(team, user);
            model.addAttribute("team", team);
            model.addAttribute("games", games);
            return "team/team-member-apply";
        }

        teamUserService.save(teamUserCreationDto, team, username);
        return "redirect:/team/" + teamId;
    }

    @PostMapping("/team/{teamId}/member/remove/{teamUserId}")
    public String removeMemberFromTeam(@PathVariable Long teamId, @PathVariable Long teamUserId,
                                       Principal principal) {
        String username = principal.getName();
        teamUserService.deleteWithAuthorization(teamUserId, username);
        return "redirect:/team/" + teamId;
    }

    @PostMapping("/user/{userId}/team/leave/{teamUserId}")
    public String leaveTeamByUser(@PathVariable Long userId, @PathVariable Long teamUserId,
                                       Principal principal) {
        String username = principal.getName();
        teamUserService.deleteWithAuthorization(teamUserId, username);
        return "redirect:/user/" + userId;
    }

}
