package pjatk.s18617.tournamentmanagement.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pjatk.s18617.tournamentmanagement.model.Match;
import pjatk.s18617.tournamentmanagement.model.User;
import pjatk.s18617.tournamentmanagement.services.GameService;
import pjatk.s18617.tournamentmanagement.services.MatchService;
import pjatk.s18617.tournamentmanagement.services.TournamentService;
import pjatk.s18617.tournamentmanagement.services.UserService;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class MatchController {

    private final TournamentService tournamentService;
    private final GameService gameService;
    private final UserService userService;
    private final MatchService matchService;

    @PostMapping("/match/{matchId}/delete")
    public String deleteMatch(@PathVariable Long matchId, Principal principal) {
        String currentUsername = principal.getName();
        Match match = matchService.findById(matchId).orElseThrow(NotFoundException::new);
        Long tournamentId = match.getTournament().getId();
        matchService.deleteWithAuthorization(match, currentUsername);
        return "redirect:/tournament/" + tournamentId;
    }

}
