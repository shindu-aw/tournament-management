package pjatk.s18617.tournamentmanagement.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjatk.s18617.tournamentmanagement.model.Announcement;

import java.io.Serializable;

/**
 * DTO for {@link pjatk.s18617.tournamentmanagement.model.Announcement}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementCreationDto implements Serializable {

    @NotNull(message = "nie może być puste")
    @Size(max = 50, message = "nie może być dłuższe od 50 znaków")
    @NotBlank(message = "nie może być puste")
    private String title;

    @NotNull(message = "nie może być puste")
    @Size(max = 1000, message = "nie może być dłuższe od 1000 znaków")
    @NotBlank(message = "nie może być puste")
    private String description;

}