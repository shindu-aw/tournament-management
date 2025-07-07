package pjatk.s18617.tournamentmanagement.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pjatk.s18617.tournamentmanagement.dtos.TournamentCreationDto;
import pjatk.s18617.tournamentmanagement.dtos.TournamentEditDto;
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
                .sorted(Comparator.comparing(Match::getDate)).toList().reversed();
        List<TournamentTeam> sortedTeamRegistrations = tournament.getTeamRegistrations().stream()
                .sorted(Comparator.comparingInt(TournamentTeam::getScoreSum)).toList().reversed();
        model.addAttribute("tournament", tournament);
        model.addAttribute("sortedMatches", sortedMatches);
        model.addAttribute("sortedTeamRegistrations", sortedTeamRegistrations);
        return "tournament";
    }


    @GetMapping("/tournament/{tournamentId}/edit")
    public String showTournamentEditForm(@PathVariable Long tournamentId, Principal principal, Model model) {
        Tournament tournament = tournamentService.getById(tournamentId).orElseThrow(NotFoundException::new);

        User currentUser = userService.findByUsername(principal.getName()).orElseThrow(NotFoundException::new);
        if (!currentUser.equals(tournament.getUserOwner()) && !currentUser.isAdmin())
            throw new AccessDeniedException("Nie masz dostępu do tego turnieju.");

        model.addAttribute("tournamentEditDto", new TournamentEditDto(
                tournament.getId(), tournament.getName(), tournament.getDescription(),
                tournament.getStartDate(), tournament.getEndDate()
        ));
        return "tournament-edit";
    }

    @PostMapping("/tournament/{tournamentId}/edit")
    public String processTournamentEditForm(@PathVariable Long tournamentId, Principal principal,
                                            @Valid TournamentEditDto tournamentEditDto, BindingResult result,
                                            Model model) {
        Tournament tournament = tournamentService.getById(tournamentId).orElseThrow(NotFoundException::new);
        User currentUser = userService.findByUsername(principal.getName()).orElseThrow(NotFoundException::new);
        if (!currentUser.equals(tournament.getUserOwner()) && !currentUser.isAdmin())
            throw new AccessDeniedException("Nie masz dostępu do tego turnieju.");

        if (result.hasErrors()) {
            model.addAttribute("tournamentEditDto", tournamentEditDto);
            return "tournament-edit";
        }

        tournamentService.update(tournament, tournamentEditDto);

        return "redirect:/tournament/" + tournamentId;
    }


    @PostMapping("/tournament/{tournamentId}/delete")
    public String deleteTournament(@PathVariable Long tournamentId, Principal principal,
                                   RedirectAttributes redirectAttributes) {
        Tournament tournament = tournamentService.getById(tournamentId).orElseThrow(NotFoundException::new);
        String currentUserName = principal.getName();
        User currentUser = userService.findByUsername(currentUserName).orElseThrow(NotFoundException::new);

        if (!currentUser.equals(tournament.getUserOwner()) && !currentUser.isAdmin())
            throw new AccessDeniedException("Nie masz dostępu do tego turnieju.");

        if (!tournamentService.deleteById(tournamentId))
            throw new NotFoundException();

        String message = "Turniej '" + tournament.getName() + "' usunięty.";
        redirectAttributes.addAttribute("message", message);
        return "redirect:/";
    }

    @PostMapping("/tournament/{tournamentId}/regenerate-secret-codes")
    public String regenerateSecretCodes(@PathVariable Long tournamentId, Principal principal) {
        Tournament tournament = tournamentService.getById(tournamentId).orElseThrow(NotFoundException::new);
        String currentUserName = principal.getName();

        tournamentService.regenerateSecretCodesWithAuthorization(tournament, currentUserName);

        return "redirect:/tournament/" + tournamentId;
    }


}
