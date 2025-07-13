package pjatk.s18617.tournamentmanagement.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
public class MatchCreationDto implements Serializable {

    @PositiveOrZero
    private Integer team1Score = 0;

    @PositiveOrZero
    private Integer team2Score = 0;

    @NotNull(message = "nie może być puste")
    private LocalDate date;

    @NotNull(message = "nie może być puste")
    private Long tournamentId;

    @NotNull(message = "nie może być puste")
    private Long tournamentTeam1Id;

    @NotNull(message = "nie może być puste")
    private Long tournamentTeam2Id;

}