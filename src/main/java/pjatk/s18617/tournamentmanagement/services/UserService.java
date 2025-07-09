package pjatk.s18617.tournamentmanagement.services;

import pjatk.s18617.tournamentmanagement.dtos.UserEditDto;
import pjatk.s18617.tournamentmanagement.dtos.UserRegistrationDto;
import pjatk.s18617.tournamentmanagement.model.User;

import java.util.Optional;

public interface UserService {

    void checkAdminAuthorization(User user);

    void checkAdminAuthorization(String username);

    void checkEditUserAuthorization(User user, String currentUserName);

    User save(User user);

    User register(UserRegistrationDto userRegistrationDto);

    Optional<User> findByUsername(String username);

    Optional<User> findById(Long userId);

    User updateUserWithAuthorization(User user, UserEditDto userEditDto, String currentUserName);
}
