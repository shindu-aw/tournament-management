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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;
import pjatk.s18617.tournamentmanagement.controllers.NotFoundException;
import pjatk.s18617.tournamentmanagement.dtos.UserEditDto;
import pjatk.s18617.tournamentmanagement.dtos.UserRegistrationDto;
import pjatk.s18617.tournamentmanagement.model.User;
import pjatk.s18617.tournamentmanagement.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Page<User> searchPage(String username, String teamName, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(
                pageNumber - 1,
                pageSize,
                Sort.by(Sort.Direction.ASC, "username")
        );

        Specification<User> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(username))
                predicates.add(criteriaBuilder.like(root.get("username"), "%" + username + "%"));
            if (StringUtils.hasText(teamName)) {
                Join<Object, Object> teamRegistrationsJoin = root.join("teamRegistrations", JoinType.LEFT);
                Predicate registrationPredicate = criteriaBuilder.like(
                        teamRegistrationsJoin.get("team").get("name"),
                        "%" + teamName + "%"
                );
                query.distinct(true); // to avoid duplicates because of join operation
                predicates.add(registrationPredicate);
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return userRepository.findAll(specification, pageRequest);
    }

    @Override
    public void checkAdminAuthorization(User user) {
        boolean userIsNotAdmin = !user.isAdmin();
        if (userIsNotAdmin)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Nie masz praw administratora.");
    }

    @Override
    public void checkAdminAuthorization(String username) {
        User user = userRepository.findByUsernameIgnoreCase(username).orElseThrow(NotFoundException::new);
        checkAdminAuthorization(user);
    }

    @Override
    public void checkEditUserAuthorization(User user, String currentUserName) {
        User currentUser = userRepository.findByUsernameIgnoreCase(currentUserName).orElseThrow(NotFoundException::new);
        boolean currentUserIsNotAdmin = !currentUser.isAdmin();
        boolean currentUserIsNotEditedUser = !currentUserName.equals(user.getUsername());
        if (currentUserIsNotAdmin && currentUserIsNotEditedUser)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Nie masz do zarządzania tym użytkownikiem.");
    }

    @Override
    public User register(UserRegistrationDto userRegistrationDto) {
        User newUser = User.builder()
                .username(userRegistrationDto.getUsername())
                .password(userRegistrationDto.getPassword())
                .build();
        return this.save(newUser);
    }

    @Override
    public User save(User user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsernameIgnoreCase(username);
    }

    @Override
    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public User updateUserWithAuthorization(User user, UserEditDto userEditDto, String currentUserName) {
        checkEditUserAuthorization(user, currentUserName);

        user.setDescription(userEditDto.getDescription());

        if (!userEditDto.getPassword().isEmpty())
            user.setPassword(userEditDto.getPassword());

        return save(user);
    }

}
