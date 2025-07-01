package pjatk.s18617.tournamentmanagement.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link pjatk.s18617.tournamentmanagement.model.User}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationDto implements Serializable {

    @NotNull
    @Size(min = 1, max = 20)
    @NotEmpty
    private String username;

    @NotNull
    @Size(min = 8, max = 30)
    @NotEmpty
    private String password;

}