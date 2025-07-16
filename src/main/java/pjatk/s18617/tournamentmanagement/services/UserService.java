package pjatk.s18617.tournamentmanagement.services;

import org.springframework.data.domain.Page;
import pjatk.s18617.tournamentmanagement.dtos.UserEditDto;
import pjatk.s18617.tournamentmanagement.dtos.UserRegistrationDto;
import pjatk.s18617.tournamentmanagement.model.User;

import java.util.Optional;

public interface UserService {

    Page<User> searchPage(String username, String teamName, Integer pageNumber, Integer pageSize);

    void checkAdminAuthorization(User user);

    void checkAdminAuthorization(String username);

    void checkEditUserAuthorization(User user, String currentUserName);

    User save(User user);

    User register(UserRegistrationDto userRegistrationDto);

    Optional<User> findByUsername(String username);

    Optional<User> findById(Long userId);

    User updateUserWithAuthorization(User user, UserEditDto userEditDto, String currentUserName);
}
