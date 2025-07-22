package pjatk.s18617.tournamentmanagement.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordDto implements Serializable {
    @NotNull(message = "nie może być puste")
    @Size(min = 8, max = 30, message = "nie może być krótsze od 8 i dłuższe od 30 znaków")
    @NotEmpty(message = "nie może być puste")
    private String password;

    @NotNull(message = "nie może być puste")
    @Size(min = 8, max = 30, message = "nie może być krótsze od 8 i dłuższe od 30 znaków")
    @NotEmpty(message = "nie może być puste")
    private String passwordRepeat;
}
