package pjatk.s18617.tournamentmanagement.dtos;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjatk.s18617.tournamentmanagement.model.Game;

import java.io.Serializable;

/**
 * DTO for {@link pjatk.s18617.tournamentmanagement.model.Game}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameCreationDto implements Serializable {

    @Size(min = 1, max = 50)
    private String name;

    @Size(max = 1000)
    private String description;

}