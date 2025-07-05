package pjatk.s18617.tournamentmanagement.services;

import pjatk.s18617.tournamentmanagement.controllers.NotFoundException;
import pjatk.s18617.tournamentmanagement.dtos.TournamentCreationDto;
import pjatk.s18617.tournamentmanagement.dtos.TournamentEditDto;
import pjatk.s18617.tournamentmanagement.model.Game;
import pjatk.s18617.tournamentmanagement.model.Tournament;
import pjatk.s18617.tournamentmanagement.model.User;

import java.util.List;
import java.util.Optional;

public interface TournamentService {

    Tournament save(TournamentCreationDto tournamentCreationDto, Game game, User userOwner);

    List<Tournament> listByGame(Game game);

    List<Tournament> listByGameId(Long gameId);

    Optional<Tournament> getById(Long id);

    Optional<Tournament> getWholeById(Long id);

    boolean deleteById(Long id);

    Tournament update(Tournament tournament, TournamentEditDto tournamentEditDto);

}
