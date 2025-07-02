package pjatk.s18617.tournamentmanagement.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link pjatk.s18617.tournamentmanagement.model.Location}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationCreationDto implements Serializable {

    @NotNull
    @Size(max = 100)
    @NotBlank
    private String country;

    @NotNull
    @Pattern(regexp = "^[A-Za-z0-9\\- ]{3,10}$")
    @NotBlank
    private String postalCode;

    @NotNull
    @Size(max = 100)
    @NotBlank
    private String city;

    @NotNull
    @Size(max = 100)
    @NotBlank
    private String street;

    @NotNull
    @Size(max = 10)
    @NotBlank
    private String houseNumber;

}