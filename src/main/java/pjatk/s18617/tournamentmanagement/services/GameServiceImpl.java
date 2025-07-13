package pjatk.s18617.tournamentmanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pjatk.s18617.tournamentmanagement.model.Game;
import pjatk.s18617.tournamentmanagement.model.GameCreationDto;
import pjatk.s18617.tournamentmanagement.model.Team;
import pjatk.s18617.tournamentmanagement.model.User;
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
