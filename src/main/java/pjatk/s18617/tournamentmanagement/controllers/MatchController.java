package pjatk.s18617.tournamentmanagement.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pjatk.s18617.tournamentmanagement.dtos.MatchCreationDto;
import pjatk.s18617.tournamentmanagement.dtos.MatchEditDto;
import pjatk.s18617.tournamentmanagement.model.Match;
import pjatk.s18617.tournamentmanagement.model.Tournament;
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


    @GetMapping("/tournament/{tournamentId}/new/match")
    public String showMatchCreationForm(@PathVariable Long tournamentId, Model model, Principal principal) {
        Tournament tournament = tournamentService.getById(tournamentId).orElseThrow(NotFoundException::new);

        String username = principal.getName();
        matchService.checkAuthorization(tournament, username);

        MatchCreationDto matchCreationDto = new MatchCreationDto();
        matchCreationDto.setTournamentId(tournamentId);

        model.addAttribute("matchCreationDto", matchCreationDto);
        model.addAttribute("tournament", tournament);

        return "match-add";
    }

    @PostMapping("/tournament/{tournamentId}/new/match")
    public String processMatchCreationForm(@PathVariable Long tournamentId, Principal principal, Model model,
                                           @Valid MatchCreationDto matchCreationDto, BindingResult result) {
        Tournament tournament = tournamentService.getById(tournamentId).orElseThrow(NotFoundException::new);

        if (result.hasErrors()) {
            model.addAttribute("tournament", tournament);
            return "match-add";
        }

        String currentUsername = principal.getName();
        Match newMatch = matchService.saveWithAuthorization(matchCreationDto, tournament, currentUsername);
        return "redirect:/tournament/" + tournamentId;
    }

    @GetMapping("/match/{matchId}/edit")
    public String showMatchEditForm(@PathVariable Long matchId, Principal principal, Model model) {
        Match match = matchService.findById(matchId).orElseThrow(NotFoundException::new);

        String username = principal.getName();
        matchService.checkAuthorization(match.getTournament(), username);

        MatchEditDto matchEditDto = new MatchEditDto(
                match.getId(),
                match.getTeam1Score(),
                match.getTeam2Score(),
                match.getDate(),
                match.getTournament().getId(),
                match.getTournamentTeam1().getId(),
                match.getTournamentTeam2().getId()
        );

        model.addAttribute("matchEditDto", matchEditDto);
        model.addAttribute("tournament", match.getTournament());
        return "match-edit";
    }

    @PostMapping("/match/{matchId}/edit")
    public String processMatchEditForm(@PathVariable Long matchId, Principal principal, Model model,
                                       @Valid MatchEditDto matchEditDto, BindingResult result) {
        Match match = matchService.findById(matchId).orElseThrow(NotFoundException::new);
        String username = principal.getName();

        if (result.hasErrors()) {
            model.addAttribute("matchEditDto", matchEditDto);
            model.addAttribute("tournament", match.getTournament());
            return "match-edit";
        }

        matchService.updateWithAuthorization(match, matchEditDto, username);
        return "redirect:/tournament/" + match.getTournament().getId();
    }

}
