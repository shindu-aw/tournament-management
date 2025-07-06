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

    @NotNull(message = "nie może być puste")
    @Size(max = 100, message = "nie może być dłuższe od 100 znaków")
    @NotBlank(message = "nie może być puste")
    private String country;

    @NotNull(message = "nie może być puste")
    @Pattern(regexp = "^[A-Za-z0-9\\- ]{3,10}$", message = "musi być prawidłowym kodem pocztowym")
    @NotBlank(message = "nie może być puste")
    private String postalCode;

    @NotNull(message = "nie może być puste")
    @Size(max = 100, message = "nie może być dłuższe od 100 znaków")
    @NotBlank(message = "nie może być puste")
    private String city;

    @NotNull(message = "nie może być puste")
    @Size(max = 100, message = "nie może być dłuższe od 100 znaków")
    @NotBlank(message = "nie może być puste")
    private String street;

    @NotNull(message = "nie może być puste")
    @Size(max = 10, message = "nie może być dłuższe od 10 znaków")
    @NotBlank(message = "nie może być puste")
    private String houseNumber;

}