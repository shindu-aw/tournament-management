package pjatk.s18617.tournamentmanagement.services;

import pjatk.s18617.tournamentmanagement.dtos.UserRegistrationDto;
import pjatk.s18617.tournamentmanagement.model.User;

import java.util.Optional;

public interface UserService {

    User save(User user);

    User register(UserRegistrationDto userRegistrationDto);

    Optional<User> findByUsername(String username);

    Optional<User> findById(Long userId);
}
