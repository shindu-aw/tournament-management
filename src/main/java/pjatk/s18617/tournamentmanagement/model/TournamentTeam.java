package pjatk.s18617.tournamentmanagement.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tournament_team")
public class TournamentTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tournament_team_seq")
    @SequenceGenerator(name = "tournament_team_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Builder.Default
    @Column(name = "score_sum")
    private Integer scoreSum = 0;

    @ManyToOne
    @JoinColumn(name = "tournament_id")
    private Tournament tournament;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    // the two reverse associations below are added solely for easy leftover match removal
    // CascadeType.REMOVE here causes all matches connected to this team registration to be deleted

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @OneToMany(mappedBy = "tournamentTeam1", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Match> matchesAsTeam1 = new LinkedHashSet<>(); // reverse association added solely for cascade.remove

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @OneToMany(mappedBy = "tournamentTeam2", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Match> matchesAsTeam2 = new LinkedHashSet<>(); // reverse association added solely for cascade.remove

}