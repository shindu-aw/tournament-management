package pjatk.s18617.tournamentmanagement.services;

import pjatk.s18617.tournamentmanagement.model.Game;

import java.util.List;
import java.util.Optional;

public interface GameService {

    Optional<Game> getById(Long id);

    List<Game> getGamesList();

}
