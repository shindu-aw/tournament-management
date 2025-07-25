package pjatk.s18617.tournamentmanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "location")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "location_seq")
    @SequenceGenerator(name = "location_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(name = "country", nullable = false, length = 100)
    private String country;

    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9\\- ]{3,10}$")
    @Column(name = "postal_code", nullable = false, length = 10)
    private String postalCode;

    @NotBlank
    @Size(max = 100)
    @Column(name = "city", nullable = false, length = 100)
    private String city;

    @NotBlank
    @Size(max = 100)
    @Column(name = "street", nullable = false, length = 100)
    private String street;

    @NotBlank
    @Size(max = 10)
    @Column(name = "house_number", nullable = false, length = 10)
    private String houseNumber;

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Location location)) return false;

        return getId().equals(location.getId()) && getCountry().equals(location.getCountry())
                && getPostalCode().equals(location.getPostalCode()) && getCity().equals(location.getCity())
                && getStreet().equals(location.getStreet()) && getHouseNumber().equals(location.getHouseNumber());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getCountry().hashCode();
        result = 31 * result + getPostalCode().hashCode();
        result = 31 * result + getCity().hashCode();
        result = 31 * result + getStreet().hashCode();
        result = 31 * result + getHouseNumber().hashCode();
        return result;
    }
}