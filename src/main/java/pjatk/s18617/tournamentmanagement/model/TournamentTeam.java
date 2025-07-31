package pjatk.s18617.tournamentmanagement.model;

import jakarta.persistence.*;
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
@Table(name = "tournament_team")
public class TournamentTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tournament_team_seq")
    @SequenceGenerator(name = "tournament_team_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Builder.Default
    @Column(name = "score_sum", nullable = false)
    private Integer scoreSum = 0;

    @ManyToOne
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    // the two reverse associations below are added solely for easy leftover match removal
    // CascadeType.REMOVE here causes all matches connected to this team registration to be deleted together with it

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @OneToMany(mappedBy = "tournamentTeam1", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Match> matchesAsTeam1 = new LinkedHashSet<>(); // reverse association added solely for cascade.remove

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @OneToMany(mappedBy = "tournamentTeam2", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<Match> matchesAsTeam2 = new LinkedHashSet<>(); // reverse association added solely for cascade.remove

    public String getTournamentName() {
        return tournament.getName();
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof TournamentTeam that)) return false;

        return getId().equals(that.getId())
                && Objects.equals(getScoreSum(), that.getScoreSum())
                && Objects.equals(getTournament(), that.getTournament())
                && Objects.equals(getTeam(), that.getTeam());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + Objects.hashCode(getScoreSum());
        result = 31 * result + Objects.hashCode(getTournament());
        result = 31 * result + Objects.hashCode(getTeam());
        return result;
    }
}