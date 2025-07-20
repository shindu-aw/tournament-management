package pjatk.s18617.tournamentmanagement.model;

import jakarta.persistence.*;
import lombok.*;
import pjatk.s18617.tournamentmanagement.controllers.NotFoundException;

import java.time.LocalDate;
import java.util.Arrays;

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

}