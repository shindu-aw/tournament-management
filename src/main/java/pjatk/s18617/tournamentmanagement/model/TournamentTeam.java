package pjatk.s18617.tournamentmanagement.model;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "score_sum")
    private Integer scoreSum;

    @ManyToOne
    @JoinColumn(name = "tournament_id")
    private Tournament tournament; // bidirectional TODO maintain relationships

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team; // bidirectional TODO maintain relationships

}