package pjatk.s18617.tournamentmanagement.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

    @Column(name = "team_1_score")
    private Integer team1Score;

    @Column(name = "team_2_score")
    private Integer team2Score;

    @Column(name = "date")
    private LocalDateTime date;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;

    @ManyToOne(optional = false)
    @JoinColumn(name = "team_1_id", nullable = false)
    private TournamentTeam team1;

    @ManyToOne
    @JoinColumn(name = "tournament_team_1_id")
    private TournamentTeam tournamentTeam1;

    @ManyToOne
    @JoinColumn(name = "tournament_team_2_id")
    private TournamentTeam tournamentTeam2;

    @ManyToOne
    @JoinColumn(name = "tournament_team_winner_id")
    private TournamentTeam tournamentTeamWinner;

}