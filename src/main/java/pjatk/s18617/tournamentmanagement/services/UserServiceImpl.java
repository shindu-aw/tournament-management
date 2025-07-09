package pjatk.s18617.tournamentmanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pjatk.s18617.tournamentmanagement.controllers.NotFoundException;
import pjatk.s18617.tournamentmanagement.dtos.UserEditDto;
import pjatk.s18617.tournamentmanagement.dtos.UserRegistrationDto;
import pjatk.s18617.tournamentmanagement.model.User;
import pjatk.s18617.tournamentmanagement.repositories.UserRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public void checkAdminAuthorization(User user) {
        boolean userIsNotAdmin = !user.isAdmin();
        if (userIsNotAdmin)
            throw new AccessDeniedException("Nie masz praw administratora.");
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
        System.out.println("sigiemka");
        System.out.println("currentUsername: " + currentUserName);
        System.out.println("user name: " + user.getUsername());
        System.out.println("is not admin: " + currentUserIsNotAdmin);
        System.out.println("is not edit user: " + currentUserIsNotEditedUser);
        if (currentUserIsNotAdmin && currentUserIsNotEditedUser)
            throw new AccessDeniedException("Nie masz do zarządzania tym użytkownikiem.");
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
