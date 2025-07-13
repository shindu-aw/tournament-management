package pjatk.s18617.tournamentmanagement.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link pjatk.s18617.tournamentmanagement.model.TeamUser}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamUserCreationDto implements Serializable {

    @NotNull(message = "nie może być puste")
    private Long gameId;

    @NotNull(message = "nie może być puste")
    @Size(min = 8, max = 8, message = "musi mieć długość dokładnie 8 znaków")
    private String secretCode;
}