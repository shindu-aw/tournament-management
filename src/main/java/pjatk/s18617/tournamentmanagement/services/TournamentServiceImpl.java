package pjatk.s18617.tournamentmanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import pjatk.s18617.tournamentmanagement.controllers.NotFoundException;
import pjatk.s18617.tournamentmanagement.dtos.TournamentCreationDto;
import pjatk.s18617.tournamentmanagement.dtos.TournamentEditDto;
import pjatk.s18617.tournamentmanagement.model.Game;
import pjatk.s18617.tournamentmanagement.model.Tournament;
import pjatk.s18617.tournamentmanagement.model.User;
import pjatk.s18617.tournamentmanagement.repositories.GameRepository;
import pjatk.s18617.tournamentmanagement.repositories.TournamentRepository;
import pjatk.s18617.tournamentmanagement.repositories.UserRepository;
import pjatk.s18617.tournamentmanagement.utils.SecretCodeGenerator;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TournamentServiceImpl implements TournamentService {

    private final UserRepository userRepository;
    private final TournamentRepository tournamentRepository;
    private final GameRepository gameRepository;
    private final UserService userService;

    @Override
    public void checkAuthorization(Tournament tournament, User user) {
        boolean userIsNotAdmin = !user.isAdmin();
        boolean userIsNotOwner = !user.equals(tournament.getUserOwner());
        boolean cannotManageTournament = userIsNotAdmin && userIsNotOwner;
        if (cannotManageTournament)
            throw new AccessDeniedException("Nie masz praw do zarządzania tym turniejem.");
    }

    @Override
    public void checkAuthorization(Tournament tournament, String username) {
        User user = userService.findByUsername(username).orElseThrow(NotFoundException::new);
        checkAuthorization(tournament, user);
    }

    @Override
    public Tournament save(TournamentCreationDto tournamentCreationDto, Game game, User userOwner) {
        Tournament newTournament = Tournament.builder()
                .name(tournamentCreationDto.getName())
                .description(tournamentCreationDto.getDescription())
                .startDate(tournamentCreationDto.getStartDate())
                .endDate(tournamentCreationDto.getEndDate())
                .userOwner(userOwner)
                .game(game)
                .joinSecretCode(SecretCodeGenerator.generateSecretCode())
                .manageSecretCode(SecretCodeGenerator.generateSecretCode())
                .build();
        return tournamentRepository.save(newTournament);
    }

    @Override
    public List<Tournament> listByGame(Game game) {
        return tournamentRepository.findByGame(game);
    }

    @Override
    public List<Tournament> listByGameId(Long gameId) {
        return tournamentRepository.findByGame_Id(gameId);
    }

    @Override
    public Optional<Tournament> getById(Long id) {
        return tournamentRepository.findById(id);
    }

    @Override
    public Optional<Tournament> getWholeById(Long id) {
        return tournamentRepository.findWholeById(id);
    }

    @Override
    public boolean deleteById(Long id) {
        if (tournamentRepository.existsById(id)) {
            tournamentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Tournament update(Tournament tournament, TournamentEditDto tournamentEditDto) {
        tournament.setName(tournamentEditDto.getName());
        tournament.setDescription(tournamentEditDto.getDescription());
        tournament.setStartDate(tournamentEditDto.getStartDate());
        tournament.setEndDate(tournamentEditDto.getEndDate());
        return tournamentRepository.save(tournament);
    }

    @Override
    public Tournament regenerateSecretCodesWithAuthorization(Tournament tournament, String username) {
        checkAuthorization(tournament, username);

        tournament.setJoinSecretCode(SecretCodeGenerator.generateSecretCode());
        tournament.setManageSecretCode(SecretCodeGenerator.generateSecretCode());

        return tournamentRepository.save(tournament);
    }


}
