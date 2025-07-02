package pjatk.s18617.tournamentmanagement.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueUsernameValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueUsername {
    String message() default "nazwa użytkownika musi być unikalna";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
