package pjatk.s18617.tournamentmanagement.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pjatk.s18617.tournamentmanagement.dtos.TournamentCreationDto;
import pjatk.s18617.tournamentmanagement.model.*;
import pjatk.s18617.tournamentmanagement.services.GameService;
import pjatk.s18617.tournamentmanagement.services.TournamentService;
import pjatk.s18617.tournamentmanagement.services.UserService;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class TournamentController {

    private final TournamentService tournamentService;
    private final GameService gameService;
    private final UserService userService;

    @GetMapping("/tournaments/{gameId}")
    public String showTournaments(@PathVariable Long gameId, Model model) {
        Game game = gameService.getById(gameId).orElseThrow(NotFoundException::new);
        List<Tournament> tournaments = tournamentService.listByGame(game);

        model.addAttribute("tournaments", tournaments);
        model.addAttribute("game", game);
        return "tournaments";
    }

    @GetMapping("/tournament/new/{gameId}")
    public String showTournamentCreationForm(@PathVariable Long gameId, Model model) {
        model.addAttribute("tournamentCreationDto", new TournamentCreationDto());

        Game game = gameService.getById(gameId).orElseThrow(NotFoundException::new);
        model.addAttribute("game", game);
        return "tournaments-add";
    }


    // BindingResult MUST immediately follow the @Valid annotated parameter in the method signature
    @PostMapping("/tournament/new/{gameId}")
    public String processTournamentCreationForm(@PathVariable Long gameId, Principal principal,
                                                @Valid TournamentCreationDto tournamentCreationDto,
                                                BindingResult result, Model model) {
        if (result.hasErrors()) {
            Game game = gameService.getById(gameId).orElseThrow(NotFoundException::new);
            model.addAttribute("game", game);
            return "tournaments-add";
        }
        String currentUserName = principal.getName();
        User currentUser = userService.findByUsername(currentUserName).orElseThrow(NotFoundException::new);
        Game currentGame = gameService.getById(gameId).orElseThrow(NotFoundException::new);
        Tournament newTournament = tournamentService.save(tournamentCreationDto, currentGame, currentUser);
        return "redirect:/tournament/" + newTournament.getId();
    }


    @GetMapping("/tournament/{tournamentId}")
    public String showTournament(@PathVariable Long tournamentId, Model model) {
        Tournament tournament = tournamentService.getById(tournamentId).orElseThrow(NotFoundException::new);
        // in-memory sort instead of DB call because they're small data sets
        List<Match> sortedMatches = tournament.getMatches().stream()
                .sorted(Comparator.comparing(Match::getDate)).toList();
        List<TournamentTeam> sortedTeamRegistrations = tournament.getTeamRegistrations().stream()
                .sorted(Comparator.comparingInt(TournamentTeam::getScoreSum)).toList().reversed();
        model.addAttribute("tournament", tournament);
        model.addAttribute("sortedMatches", sortedMatches);
        model.addAttribute("sortedTeamRegistrations", sortedTeamRegistrations);
        return "tournament";
    }

}
