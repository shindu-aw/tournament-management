package pjatk.s18617.tournamentmanagement.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pjatk.s18617.tournamentmanagement.dtos.TournamentCreationDto;
import pjatk.s18617.tournamentmanagement.dtos.TournamentEditDto;
import pjatk.s18617.tournamentmanagement.model.*;
import pjatk.s18617.tournamentmanagement.repositories.TournamentRepository;
import pjatk.s18617.tournamentmanagement.services.GameService;
import pjatk.s18617.tournamentmanagement.services.TournamentService;
import pjatk.s18617.tournamentmanagement.services.UserService;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
public class TournamentController {

    private final TournamentService tournamentService;
    private final GameService gameService;
    private final UserService userService;


    @GetMapping("/game/{gameId}/tournaments")
    public String showTournamentsList(@PathVariable Long gameId, Model model,
                                      @RequestParam(value = "currentPage", defaultValue = "1") int currentPage,
                                      @RequestParam(value = "tournamentName", required = false) String tournamentName,
                                      @RequestParam(value = "ownerUsername", required = false) String ownerUsername,
                                      @RequestParam(value = "beforeDate", required = false) LocalDate beforeDate,
                                      @RequestParam(value = "afterDate", required = false) LocalDate afterDate) {
        Game game = gameService.getById(gameId).orElseThrow(NotFoundException::new);

        Page<Tournament> tournamentsPage = tournamentService.searchPage(
                gameId, tournamentName, beforeDate, afterDate, ownerUsername, currentPage, 10
        );

        // page-related
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", tournamentsPage.getTotalPages());

        // content
        model.addAttribute("tournaments", tournamentsPage.getContent());
        model.addAttribute("game", game);

        // search form values
        model.addAttribute("tournamentName", tournamentName);
        model.addAttribute("ownerUsername", ownerUsername);
        model.addAttribute("beforeDate", beforeDate);
        model.addAttribute("afterDate", afterDate);

        return "tournament/tournaments-list";
    }

    @GetMapping("/tournament/new")
    public String showTournamentCreationForm(Model model) {
        List<Game> games = gameService.getGamesListSortedByName();

        model.addAttribute("games", games);
        model.addAttribute("tournamentCreationDto", new TournamentCreationDto());

        return "tournament/tournament-add";
    }


    @PostMapping("/tournament/new")
    public String processTournamentCreationForm(Principal principal, Model model,
                                                @Valid TournamentCreationDto tournamentCreationDto,
                                                BindingResult result) {
        if (result.hasErrors()) {
            List<Game> games = gameService.getGamesListSortedByName();
            model.addAttribute("games", games);
            return "tournament/tournament-add";
        }

        String currentUserName = principal.getName();
        Tournament newTournament = tournamentService.save(tournamentCreationDto, currentUserName);

        return "redirect:/tournament/" + newTournament.getId();
    }


    @GetMapping("/tournament/{tournamentId}")
    public String showTournament(@PathVariable Long tournamentId, Model model) {
        Tournament tournament = tournamentService.getById(tournamentId).orElseThrow(NotFoundException::new);

        // in-memory because it's a small data set, as well as already sorted alphabetically beforehand
        List<TournamentTeam> teamRegistrationsSortedByScoreSum = tournament.getTeamRegistrations().stream()
                .sorted(Comparator.comparing(TournamentTeam::getScoreSum, Comparator.reverseOrder())).toList();

        model.addAttribute("tournament", tournament);
        model.addAttribute("sortedMatches", tournament.getMatches());
        model.addAttribute("sortedTeamRegistrations", teamRegistrationsSortedByScoreSum);
        return "tournament/tournament";
    }


    @GetMapping("/tournament/{tournamentId}/edit")
    public String showTournamentEditForm(@PathVariable Long tournamentId, Principal principal, Model model) {
        Tournament tournament = tournamentService.getById(tournamentId).orElseThrow(NotFoundException::new);

        String username = principal.getName();
        tournamentService.checkAuthorization(tournament, username);
        tournamentService.throwBadRequestIfFinished(tournament);

        model.addAttribute("tournamentEditDto", new TournamentEditDto(
                tournament.getId(), tournament.getName(), tournament.getDescription(),
                tournament.getStartDate(), tournament.getEndDate()
        ));
        model.addAttribute("tournament", tournament);

        return "tournament/tournament-edit";
    }

    @PostMapping("/tournament/{tournamentId}/edit")
    public String processTournamentEditForm(@PathVariable Long tournamentId, Principal principal,
                                            @Valid TournamentEditDto tournamentEditDto, BindingResult result,
                                            Model model) {
        Tournament tournament = tournamentService.getById(tournamentId).orElseThrow(NotFoundException::new);

        if (result.hasErrors()) {
            model.addAttribute("tournamentEditDto", tournamentEditDto);
            model.addAttribute("tournament", tournament);
            return "tournament/tournament-edit";
        }

        String currentUsername = principal.getName();
        tournamentService.updateWithAuthorization(tournament, tournamentEditDto, currentUsername);
        return "redirect:/tournament/" + tournamentId;
    }


    @PostMapping("/tournament/{tournamentId}/delete")
    public String deleteTournament(@PathVariable Long tournamentId, Principal principal,
                                   RedirectAttributes redirectAttributes) {
        Tournament tournament = tournamentService.getById(tournamentId).orElseThrow(NotFoundException::new);
        String currentUserName = principal.getName();
        User userOwner = tournament.getUserOwner();

        String message = "Turniej '" + tournament.getName() + "' usunięty.";
        redirectAttributes.addAttribute("message", message);

        tournamentService.deleteWithAuthorization(tournament, currentUserName);
        return "redirect:/user/" + userOwner.getId();
    }

    @PostMapping("/tournament/{tournamentId}/regenerate-secret-codes")
    public String regenerateSecretCodes(@PathVariable Long tournamentId, Principal principal) {
        Tournament tournament = tournamentService.getById(tournamentId).orElseThrow(NotFoundException::new);
        String currentUserName = principal.getName();

        tournamentService.regenerateSecretCodesWithAuthorization(tournament, currentUserName);

        return "redirect:/tournament/" + tournamentId;
    }

    @PostMapping("/tournament/{tournamentId}/set-as-finished")
    public String setAsFinished(@PathVariable Long tournamentId, Principal principal) {
        String currentUserName = principal.getName();
        tournamentService.setAsFinishedWithAuthorization(tournamentId, currentUserName);

        return "redirect:/tournament/" + tournamentId;
    }


}
