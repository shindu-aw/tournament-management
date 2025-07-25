package pjatk.s18617.tournamentmanagement.services;

import org.springframework.data.domain.Page;
import pjatk.s18617.tournamentmanagement.dtos.TournamentCreationDto;
import pjatk.s18617.tournamentmanagement.dtos.TournamentEditDto;
import pjatk.s18617.tournamentmanagement.model.Game;
import pjatk.s18617.tournamentmanagement.model.Tournament;
import pjatk.s18617.tournamentmanagement.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TournamentService {

    void checkAuthorization(Tournament tournament, User user);

    void checkAuthorization(Tournament tournament, String username);

    void throwBadRequestIfFinished(Tournament tournament);

    Tournament save(TournamentCreationDto tournamentCreationDto, String userOwnerUsername);

    Page<Tournament> searchPage(Long gameId, String name, LocalDate beforeDate, LocalDate afterDate,
                                String ownerUsername, Integer pageNumber, Integer pageSize);

    List<Tournament> listByGame(Game game);

    List<Tournament> listByGameId(Long gameId);

    Optional<Tournament> getById(Long id);

    Optional<Tournament> getWholeById(Long id);

    void deleteWithAuthorization(Tournament tournament, String username);

    Tournament update(Tournament tournament, TournamentEditDto tournamentEditDto);

    Tournament updateWithAuthorization(Tournament tournament, TournamentEditDto tournamentEditDto, String username);

    Tournament regenerateSecretCodesWithAuthorization(Tournament tournament, String username);

    Tournament addUserModerator(Tournament tournament, String username);

    void removeUserModeratorWithAuthorization(Long tournamentId, Long managingUserId, String currentUserName);

    void setAsFinishedWithAuthorization(Long tournamentId, String currentUserName);

    int deleteCompletedTournamentsOlderThanWithAdminAuthorization(LocalDate date, String currentUserName);
}
