package pjatk.s18617.tournamentmanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

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
    private Set<TeamUser> userRegistrations = new LinkedHashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "team", orphanRemoval = true)
    private Set<TournamentTeam> tournamentRegistrations = new LinkedHashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Link> links = new LinkedHashSet<>();

    // checks whether the user is already a member of this team assigned to this game
    public boolean doesNotHaveUserRegisteredOnGame(User user, Game game) {
        return userRegistrations.stream().noneMatch(userRegistration ->
                userRegistration.getGame().equals(game) && userRegistration.getUser().equals(user)
        );
    }

}