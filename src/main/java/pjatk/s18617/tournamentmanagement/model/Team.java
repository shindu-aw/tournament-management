package pjatk.s18617.tournamentmanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "team")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "team_seq")
    @SequenceGenerator(name = "team_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(min = 1, max = 50)
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Size(max = 1000)
    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "secret_code", nullable = false, length = 8)
    private String secretCode;

    @ManyToOne
    @JoinColumn(name = "user_owner_id", nullable = false)
    private User userOwner;

    @Builder.Default
    @OneToMany(mappedBy = "team", orphanRemoval = true)
    @OrderBy("game ASC, team ASC, user ASC")
    private List<TeamUser> userRegistrations = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "team", orphanRemoval = true)
    @OrderBy("tournament DESC")
    private List<TournamentTeam> tournamentRegistrations = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("name")
    private List<Link> links = new ArrayList<>();

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Team team)) return false;

        return getId().equals(team.getId()) && getName().equals(team.getName())
                && Objects.equals(getDescription(), team.getDescription())
                && getSecretCode().equals(team.getSecretCode())
                && getUserOwner().equals(team.getUserOwner());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getName().hashCode();
        result = 31 * result + Objects.hashCode(getDescription());
        result = 31 * result + getSecretCode().hashCode();
        result = 31 * result + getUserOwner().hashCode();
        return result;
    }

    // checks whether the user is already a member of this team assigned to this game
    public boolean doesNotHaveUserRegisteredOnGame(User user, Game game) {
        return userRegistrations.stream().noneMatch(userRegistration ->
                userRegistration.getGame().equals(game) && userRegistration.getUser().equals(user)
        );
    }

}