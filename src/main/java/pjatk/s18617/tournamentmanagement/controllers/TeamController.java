package pjatk.s18617.tournamentmanagement.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pjatk.s18617.tournamentmanagement.model.Link;
import pjatk.s18617.tournamentmanagement.model.Team;
import pjatk.s18617.tournamentmanagement.model.TeamUser;
import pjatk.s18617.tournamentmanagement.model.TournamentTeam;
import pjatk.s18617.tournamentmanagement.services.TeamService;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

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
        return "team";
    }

}
