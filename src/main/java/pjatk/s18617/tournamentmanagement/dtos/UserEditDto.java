package pjatk.s18617.tournamentmanagement.dtos;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pjatk.s18617.tournamentmanagement.model.User;

import java.io.Serializable;

/**
 * DTO for {@link pjatk.s18617.tournamentmanagement.model.User}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEditDto implements Serializable {

    @Size(max = 30, message = "nie może być dłuższe od 30 znaków")
    private String password;
    
    @Size(max = 1000, message = "nie może być dłuższe od 1000 znaków")
    private String description;
    
}