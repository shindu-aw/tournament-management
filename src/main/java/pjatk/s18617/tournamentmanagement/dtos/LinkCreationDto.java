package pjatk.s18617.tournamentmanagement.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;
import pjatk.s18617.tournamentmanagement.model.Link;

import java.io.Serializable;

/**
 * DTO for {@link pjatk.s18617.tournamentmanagement.model.Link}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinkCreationDto implements Serializable {

    @NotNull(message = "nie może być puste")
    @Size(min = 1, max = 50, message = "nie może być krótsze od 1 i dłuższe od 100 znaków")
    @NotBlank(message = "nie może być puste")
    private String name;

    @NotNull(message = "nie może być puste")
    @Size(max = 2000, message = "nie może być dłuższe od 2000 znaków")
    @NotBlank(message = "nie może być puste")
    @URL(message = "musi być linkiem")
    private String url;

}