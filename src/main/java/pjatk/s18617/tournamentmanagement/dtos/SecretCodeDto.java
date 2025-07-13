package pjatk.s18617.tournamentmanagement.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecretCodeDto {

    @NotNull(message = "nie może być puste")
    @Size(min = 8, max = 8, message = "musi mieć długość dokładne 8 znaków")
    private String secretCode;

}
