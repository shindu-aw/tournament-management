package pjatk.s18617.tournamentmanagement.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjatk.s18617.tournamentmanagement.validation.UniqueUsername;

import java.io.Serializable;

/**
 * DTO for {@link pjatk.s18617.tournamentmanagement.model.User}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationDto implements Serializable {

    @NotNull(message = "nie może być puste")
    @Size(min = 1, max = 20, message = "nie może być krótsze od 1 i dłuższe od 20 znaków")
    @NotEmpty(message = "nie może być puste")
    @UniqueUsername
    private String username;

    @NotNull(message = "nie może być puste")
    @Size(min = 8, max = 30, message = "nie może być krótsze od 8 i dłuższe od 30 znaków")
    @NotEmpty(message = "nie może być puste")
    private String password;

    @NotNull(message = "nie może być puste")
    @Size(min = 8, max = 30, message = "nie może być krótsze od 8 i dłuższe od 30 znaków")
    @NotEmpty(message = "nie może być puste")
    private String passwordRepeat;

}