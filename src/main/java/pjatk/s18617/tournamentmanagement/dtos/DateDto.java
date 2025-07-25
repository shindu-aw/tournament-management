package pjatk.s18617.tournamentmanagement.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DateDto implements Serializable {

    @NotNull(message = "nie może być puste")
    @Past(message = "data musi być starsza od dnia dzisiejszego")
    private LocalDate date;

}
