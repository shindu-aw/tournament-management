package pjatk.s18617.tournamentmanagement.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pjatk.s18617.tournamentmanagement.model.Game;
import pjatk.s18617.tournamentmanagement.model.GameCreationDto;
import pjatk.s18617.tournamentmanagement.model.Tournament;
import pjatk.s18617.tournamentmanagement.services.GameService;
import pjatk.s18617.tournamentmanagement.services.TournamentService;
import pjatk.s18617.tournamentmanagement.services.UserService;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;
    private final UserService userService;

    @GetMapping("/game")
    public String showGames(Model model) {
        List<Game> games = gameService.getGamesList();
        model.addAttribute("games", games);
        return "games";
    }

    @GetMapping("/game/new")
    public String showGameCreationForm(Model model, Principal principal) {
        String currentUserName = principal.getName();
        userService.checkAdminAuthorization(currentUserName);
        model.addAttribute("gameCreationDto", new GameCreationDto());
        return "game-add";
    }

    @PostMapping("/game/new")
    public String processGameCreationForm(Principal principal,
                                          @Valid GameCreationDto gameCreationDto, BindingResult result) {
        if (result.hasErrors())
            return "game-add";

        String currentUserName = principal.getName();
        Game createdGame = gameService.saveWithAuthorization(gameCreationDto, currentUserName);

        return "redirect:/game/" + createdGame.getId() + "/tournaments";
    }

}
