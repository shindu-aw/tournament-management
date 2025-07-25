package pjatk.s18617.tournamentmanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "game")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "game_seq")
    @SequenceGenerator(name = "game_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(min = 1, max = 50)
    @Column(name = "name", unique = true, nullable = false, length = 50)
    private String name;

    @Size(max = 1000)
    @Column(name = "description", length = 1000)
    private String description;

    @Builder.Default
    @OneToMany(mappedBy = "game", orphanRemoval = true)
    private Set<Tournament> tournaments = new LinkedHashSet<>();

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Game game)) return false;

        return getId().equals(game.getId()) && getName().equals(game.getName()) && Objects.equals(getDescription(),
                game.getDescription());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getName().hashCode();
        result = 31 * result + Objects.hashCode(getDescription());
        return result;
    }
}