package pjatk.s18617.tournamentmanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tournament")
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tournament_seq")
    @SequenceGenerator(name = "tournament_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(min = 1, max = 50)
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Size(max = 1000)
    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Size(min = 8, max = 8)
    @Column(name = "join_secret_code", nullable = false, length = 8)
    private String joinSecretCode; // for joining teams

    @Size(min = 8, max = 8)
    @Column(name = "manage_secret_code", nullable = false, length = 8)
    private String manageSecretCode; // for user managers/moderators

    @ManyToOne
    @JoinColumn(name = "user_owner_id", nullable = false)
    private User userOwner;

    @Builder.Default
    @ManyToMany
    @JoinTable(name = "tournament_user",
            joinColumns = @JoinColumn(name = "tournament_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> usersManaging = new LinkedHashSet<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @Builder.Default
    @OneToMany(mappedBy = "tournament", orphanRemoval = true)
    private Set<TournamentTeam> teamRegistrations = new LinkedHashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "tournament", orphanRemoval = true)
    private Set<Match> matches = new LinkedHashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Announcement> announcements = new LinkedHashSet<>();

}