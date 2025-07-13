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

    @NotNull(message = "nie może być puste")
    private Long id;

    @NotNull(message = "nie może być puste")
    @Size(min = 1, max = 50, message = "nie może być krótsze od 1 i dłuższe od 50 znaków")
    @NotEmpty(message = "nie może być puste")
    private String name;

    @Size(max = 1000, message = "nie może być dłuższe od 1000 znaków")
    private String description;

    @NotNull(message = "nie może być puste")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

}