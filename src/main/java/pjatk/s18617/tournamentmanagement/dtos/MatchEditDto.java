package pjatk.s18617.tournamentmanagement.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import pjatk.s18617.tournamentmanagement.model.Tournament;
import pjatk.s18617.tournamentmanagement.model.TournamentTeam;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link pjatk.s18617.tournamentmanagement.model.Match}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchEditDto implements Serializable {

    @NotNull
    private Long id;

    @PositiveOrZero
    private Integer team1Score = 0;

    @PositiveOrZero
    private Integer team2Score = 0;

    @NotNull(message = "nie może być puste")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @NotNull
    private Long tournamentId;

    @NotNull
    private Long tournamentTeam1Id;

    @NotNull
    private Long tournamentTeam2Id;

}