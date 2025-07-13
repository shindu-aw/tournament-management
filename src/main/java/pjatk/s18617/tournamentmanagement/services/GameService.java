package pjatk.s18617.tournamentmanagement.services;

import pjatk.s18617.tournamentmanagement.model.Game;
import pjatk.s18617.tournamentmanagement.model.GameCreationDto;
import pjatk.s18617.tournamentmanagement.model.Team;
import pjatk.s18617.tournamentmanagement.model.User;

import java.util.List;
import java.util.Optional;

public interface GameService {

    Optional<Game> getById(Long id);

    List<Game> getGamesList();

    List<Game> getGamesListSortedByName();

    List<Game> getGamesUserNotRegisteredForTeam(Team team, User user);

    Game saveWithAuthorization(GameCreationDto dto, String username);

}
