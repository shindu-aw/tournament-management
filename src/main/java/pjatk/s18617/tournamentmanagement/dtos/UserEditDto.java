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

    @Size(max = 30)
    private String password;
    
    @Size(max = 1000)
    private String description;
    
}