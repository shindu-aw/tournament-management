package pjatk.s18617.tournamentmanagement.services;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;
import pjatk.s18617.tournamentmanagement.controllers.NotFoundException;
import pjatk.s18617.tournamentmanagement.dtos.TeamCreationDto;
import pjatk.s18617.tournamentmanagement.dtos.TeamEditDto;
import pjatk.s18617.tournamentmanagement.model.Team;
import pjatk.s18617.tournamentmanagement.model.Tournament;
import pjatk.s18617.tournamentmanagement.model.User;
import pjatk.s18617.tournamentmanagement.repositories.TeamRepository;
import pjatk.s18617.tournamentmanagement.utils.SecretCodeGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final UserService userService;

    @Override
    public void checkAuthorization(Team team, User user) {
        boolean userIsNotAdmin = !user.isAdmin();
        boolean userIsNotOwner = !user.equals(team.getUserOwner());
        boolean cannotManageTeam = userIsNotAdmin && userIsNotOwner;
        if (cannotManageTeam)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Nie masz praw do zarządzania tą drużyną.");
    }

    @Override
    public void checkAuthorization(Team team, String username) {
        User user = userService.findByUsername(username).orElseThrow(NotFoundException::new);
        checkAuthorization(team, user);
    }

    @Override
    public Page<Team> searchPage(String name, String ownerUsername, String registeredUsername, Integer pageNumber,
                                 Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(
                pageNumber - 1,
                pageSize,
                Sort.by(Sort.Direction.ASC, "name")
        );

        Specification<Team> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(name))
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
            if (StringUtils.hasText(ownerUsername))
                predicates.add(
                        criteriaBuilder.like(root.get("userOwner").get("username"), "%" + ownerUsername + "%")
                );
            if (StringUtils.hasText(registeredUsername)) {
                Join<Object, Object> userRegistrationsJoin = root.join("userRegistrations", JoinType.LEFT);
                Predicate registrationPredicate = criteriaBuilder.like(
                        userRegistrationsJoin.get("user").get("username"),
                        "%" + registeredUsername + "%"
                );
                query.distinct(true); // to avoid duplicates because of join operation
                predicates.add(registrationPredicate);
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return teamRepository.findAll(specification, pageRequest);
    }

    @Override
    public Optional<Team> findById(Long teamId) {
        return teamRepository.findById(teamId);
    }

    /**
     * Retrieves a list of teams owned by the specified user that are not registered in the given tournament, sorted
     * alphabetically by team name.
     *
     * <p>
     * This method performs a query to find all {@link Team} entities where the owner matches the provided
     * {@link User} and the team is not yet registered in the specified {@link Tournament}. The resulting list is
     * ordered by the team's name in ascending order.
     * </p>
     *
     * @param tournament the {@link Tournament} for which team registration is checked (must not be null)
     * @param user       the {@link User} who owns the teams to be retrieved (must not be null)
     * @return a list of {@link Team} entities owned by the user and not registered in the tournament, sorted by team
     * name in ascending order
     */
    @Override
    public List<Team> findTeamsOwnedByUserNotRegisteredInTournamentOrderByName(Tournament tournament, User user) {
        return teamRepository.findTeamsOwnedByUserNotRegisteredInTournamentOrderByName(tournament, user);
    }

    @Override
    public void deleteWithAuthorization(Team team, String username) {
        checkAuthorization(team, username);
        teamRepository.delete(team);
    }

    @Override
    public Team save(TeamCreationDto dto, User userOwner) {
        Team team = Team.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .userOwner(userOwner)
                .secretCode(SecretCodeGenerator.generateSecretCode())
                .build();
        return teamRepository.save(team);
    }

    @Override
    public Team save(TeamCreationDto dto, String userOwnerUsername) {
        User user = userService.findByUsername(userOwnerUsername).orElseThrow(NotFoundException::new);
        return save(dto, user);
    }

    @Override
    public Team updateWithAuthorization(Team updatedTeam, TeamEditDto dto, String currentUserName) {
        checkAuthorization(updatedTeam, currentUserName);

        updatedTeam.setName(dto.getName());
        updatedTeam.setDescription(dto.getDescription());

        return teamRepository.save(updatedTeam);
    }

    @Override
    public Team regenerateSecretCodeWithAuthorization(Long teamId, String currentUserName) {
        Team team = findById(teamId).orElseThrow(NotFoundException::new);
        checkAuthorization(team, currentUserName);

        team.setSecretCode(SecretCodeGenerator.generateSecretCode());

        return teamRepository.save(team);
    }

}
