package pjatk.s18617.tournamentmanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pjatk.s18617.tournamentmanagement.dtos.GameCreationDto;
import pjatk.s18617.tournamentmanagement.model.*;
import pjatk.s18617.tournamentmanagement.repositories.GameRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final UserService userService;

    @Override
    public Optional<Game> getById(Long id) {
        return gameRepository.findById(id);
    }

    @Override
    public List<Game> getGamesList() {
        return gameRepository.findAll();
    }

    @Override
    public List<Game> getGamesListSortedByName() {
        return gameRepository.findAllByOrderByNameAsc();
    }

    /**
     * Retrieves a list of {@link Game} entities for which the specified {@link User} is not registered with the
     * given {@link Team}.
     * <p>
     * This method filters out all games where the user already has a registration with the team (via a
     * {@link TeamUser} association) and returns only those games where such a registration doesn't exist. The
     * results are sorted in ascending order by the game's name.
     * </p>
     *
     * <p>
     * <b>Typical usage:</b> In a team application form, to present a list of games to which a user can still be
     * registered to a team.
     * </p>
     *
     * @param team the {@link Team} for which to check user registrations (must not be null)
     * @param user the {@link User} to check for existing game registrations (must not be null)
     * @return a list of {@link Game} entities not yet registered for the given user and team, sorted by game name in
     * ascending order
     * @see TeamUser
     * @see Game
     */
    @Override
    public List<Game> getGamesUserNotRegisteredForTeam(Team team, User user) {
        return gameRepository.findGamesUserNotRegisteredForTeam(team, user);
    }

    @Override
    public Game saveWithAuthorization(GameCreationDto dto, String username) {
        userService.checkAdminAuthorization(username);
        Game game = Game.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
        return gameRepository.save(game);
    }

}
