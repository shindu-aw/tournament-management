package pjatk.s18617.tournamentmanagement.model;

import jakarta.persistence.*;
import lombok.*;
import pjatk.s18617.tournamentmanagement.controllers.NotFoundException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "match_entry")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "match_seq")
    @SequenceGenerator(name = "match_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Builder.Default
    @Column(name = "team_1_score")
    private Integer team1Score = 0;

    @Builder.Default
    @Column(name = "team_2_score")
    private Integer team2Score = 0;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;

    @ManyToOne
    @JoinColumn(name = "tournament_team_1_id")
    private TournamentTeam tournamentTeam1;

    @ManyToOne
    @JoinColumn(name = "tournament_team_2_id")
    private TournamentTeam tournamentTeam2;

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Match match)) return false;

        return getId().equals(match.getId()) && Objects.equals(getTeam1Score(), match.getTeam1Score())
                && Objects.equals(getTeam2Score(), match.getTeam2Score()) && getDate().equals(match.getDate())
                && getTournament().equals(match.getTournament())
                && Objects.equals(getTournamentTeam1(), match.getTournamentTeam1())
                && Objects.equals(getTournamentTeam2(), match.getTournamentTeam2());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + Objects.hashCode(getTeam1Score());
        result = 31 * result + Objects.hashCode(getTeam2Score());
        result = 31 * result + getDate().hashCode();
        result = 31 * result + getTournament().hashCode();
        result = 31 * result + Objects.hashCode(getTournamentTeam1());
        result = 31 * result + Objects.hashCode(getTournamentTeam2());
        return result;
    }
}