package pjatk.s18617.tournamentmanagement.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import pjatk.s18617.tournamentmanagement.repositories.UserRepository;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if (username == null)
            return true; // let @NotNull handle this
        return !userRepository.existsByUsernameIgnoreCase(username);
    }
}
