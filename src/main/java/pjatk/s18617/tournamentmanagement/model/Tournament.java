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

    @Column(name = "secret_code", nullable = false, length = 8)
    private String secretCode;

    @ManyToOne
    @JoinColumn(name = "user_owner_id")
    private User userOwner;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @OneToMany(mappedBy = "tournament", orphanRemoval = true)
    private Set<TournamentTeam> teamRegistrations = new LinkedHashSet<>();

    @OneToMany(mappedBy = "tournament", orphanRemoval = true)
    private Set<Match> matches = new LinkedHashSet<>();

}