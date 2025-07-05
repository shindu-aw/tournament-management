package pjatk.s18617.tournamentmanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import pjatk.s18617.tournamentmanagement.controllers.NotFoundException;
import pjatk.s18617.tournamentmanagement.model.Match;
import pjatk.s18617.tournamentmanagement.model.User;
import pjatk.s18617.tournamentmanagement.repositories.MatchRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    private final UserService userService;

    @Override
    public Optional<Match> findById(Long id) {
        return matchRepository.findById(id);
    }

    public void deleteWithAuthorization(Match match, String username) {
        User user = userService.findByUsername(username).orElseThrow(NotFoundException::new);

        boolean userIsAdmin = user.isAdmin();
        boolean userIsOwner = user.equals(match.getTournament().getUserOwner());
        boolean userIsManager = match.getTournament().isManagedByUser(username);
        if (!userIsAdmin && !userIsOwner && !userIsManager)
            throw new AccessDeniedException("Nie masz praw do usunięcia tego meczu.");

        matchRepository.delete(match);
    }


        /*

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

     */

}
