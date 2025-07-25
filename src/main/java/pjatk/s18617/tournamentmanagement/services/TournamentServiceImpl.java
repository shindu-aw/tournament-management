package pjatk.s18617.tournamentmanagement.services;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;
import pjatk.s18617.tournamentmanagement.controllers.NotFoundException;
import pjatk.s18617.tournamentmanagement.dtos.TournamentCreationDto;
import pjatk.s18617.tournamentmanagement.dtos.TournamentEditDto;
import pjatk.s18617.tournamentmanagement.model.Game;
import pjatk.s18617.tournamentmanagement.model.Tournament;
import pjatk.s18617.tournamentmanagement.model.User;
import pjatk.s18617.tournamentmanagement.repositories.GameRepository;
import pjatk.s18617.tournamentmanagement.repositories.TournamentRepository;
import pjatk.s18617.tournamentmanagement.repositories.TournamentTeamRepository;
import pjatk.s18617.tournamentmanagement.repositories.UserRepository;
import pjatk.s18617.tournamentmanagement.utils.SecretCodeGenerator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TournamentServiceImpl implements TournamentService {

    private final UserRepository userRepository;
    private final TournamentRepository tournamentRepository;
    private final GameRepository gameRepository;
    private final UserService userService;
    private final TournamentTeamRepository tournamentTeamRepository;

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void checkAuthorization(Tournament tournament, User user) {
        boolean userIsNotAdmin = !user.isAdmin();
        boolean userIsNotOwner = !user.equals(tournament.getUserOwner());
        boolean cannotManageTournament = userIsNotAdmin && userIsNotOwner;
        if (cannotManageTournament)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Nie masz praw do zarządzania tym turniejem.");
    }

    @Override
    public void checkAuthorization(Tournament tournament, String username) {
        User user = userService.findByUsername(username).orElseThrow(NotFoundException::new);
        checkAuthorization(tournament, user);
    }

    @Override
    public void throwBadRequestIfFinished(Tournament tournament) {
        if (tournament.getFinished()) // if the tournament is finished
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ten turniej jest już zakończony.");
    }

    @Override
    public Tournament save(TournamentCreationDto tournamentCreationDto, String userOwnerUsername) {
        Game game = gameRepository.findById(tournamentCreationDto.getGameId()).orElseThrow(NotFoundException::new);
        User userOwner = userService.findByUsername(userOwnerUsername).orElseThrow(NotFoundException::new);
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
    public Page<Tournament> searchPage(Long gameId, String name, LocalDate beforeDate, LocalDate afterDate,
                                       String ownerUsername, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(
                pageNumber - 1,
                pageSize,
                Sort.by(Sort.Order.desc("startDate"), Sort.Order.asc("name"))
        );

        Specification<Tournament> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (gameId != null)
                predicates.add(criteriaBuilder.equal(root.get("game").get("id"), gameId));
            if (StringUtils.hasText(name))
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
            if (beforeDate != null)
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("startDate"), beforeDate));
            if (afterDate != null)
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"), afterDate));
            if (StringUtils.hasText(ownerUsername))
                predicates.add(criteriaBuilder.like(root.get("userOwner").get("username"), "%" + ownerUsername + "%"));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return tournamentRepository.findAll(specification, pageRequest);
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
    public void deleteWithAuthorization(Tournament tournament, String username) {
        checkAuthorization(tournament, username);
        tournamentRepository.delete(tournament);
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
    public Tournament updateWithAuthorization(Tournament tournament, TournamentEditDto tournamentEditDto,
                                              String username) {
        checkAuthorization(tournament, username);
        throwBadRequestIfFinished(tournament);

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

    @Override
    public Tournament addUserModerator(Tournament tournament, String username) {
        User user = userService.findByUsername(username).orElseThrow(NotFoundException::new);
        tournament.getUsersManaging().add(user);
        return tournamentRepository.save(tournament);
    }

    @Override
    public void removeUserModeratorWithAuthorization(Long tournamentId, Long managingUserId, String currentUserName) {
        Tournament tournament = tournamentRepository.findById(tournamentId).orElseThrow(NotFoundException::new);

        checkAuthorization(tournament, currentUserName);

        User userManager = userService.findById(managingUserId).orElseThrow(NotFoundException::new);
        tournament.getUsersManaging().remove(userManager);
        tournamentRepository.save(tournament);
    }

    @Transactional
    @Override
    public void setAsFinishedWithAuthorization(Long tournamentId, String currentUserName) {
        Tournament tournament = tournamentRepository.findById(tournamentId).orElseThrow(NotFoundException::new);
        checkAuthorization(tournament, currentUserName);

        // check if it's already finished in case any logic is implemented in the future that would be executed
        // when the tournament is set as finished, to avoid running it multiple times
        throwBadRequestIfFinished(tournament);

        tournamentTeamRepository.recountTournamentTeamScores(tournamentId);

        if (tournament.getEndDate() == null)
            tournament.setEndDate(LocalDate.now());
        tournament.setFinished(true);
        tournamentRepository.save(tournament);
    }

    @Transactional
    @Override
    public int deleteCompletedTournamentsOlderThanWithAdminAuthorization(LocalDate date, String currentUserName) {
        userService.checkAdminAuthorization(currentUserName);
        return Objects.requireNonNull(jdbcTemplate.queryForObject(
                "SELECT manage_tournaments_by_date(?)",
                Integer.class,
                date
        )).describeConstable().orElseThrow(NotFoundException::new);
    }


}
