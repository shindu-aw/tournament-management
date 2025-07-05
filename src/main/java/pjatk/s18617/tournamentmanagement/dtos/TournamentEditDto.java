package pjatk.s18617.tournamentmanagement.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link pjatk.s18617.tournamentmanagement.model.Tournament}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TournamentEditDto implements Serializable {

    @NotNull
    private Long id;

    @NotNull
    @Size(min = 1, max = 50)
    @NotEmpty
    private String name;

    @Size(max = 1000)
    private String description;

    @NotNull(message = "nie może być puste")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

}