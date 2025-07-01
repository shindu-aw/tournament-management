package pjatk.s18617.tournamentmanagement.services;

import pjatk.s18617.tournamentmanagement.dtos.UserRegistrationDto;
import pjatk.s18617.tournamentmanagement.model.User;

public interface UserService {

    User save(User user);

    User register(UserRegistrationDto userRegistrationDto);
}
