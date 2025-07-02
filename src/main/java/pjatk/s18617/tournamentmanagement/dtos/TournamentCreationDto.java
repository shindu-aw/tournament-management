package pjatk.s18617.tournamentmanagement.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link pjatk.s18617.tournamentmanagement.model.Tournament}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TournamentCreationDto implements Serializable {

    @NotNull
    @Size(min = 1, max = 50)
    @NotEmpty
    private String name;

    @Size(max = 1000)
    private String description;

    private LocalDate startDate;
    private LocalDate endDate;

}