package pjatk.s18617.tournamentmanagement.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "team_user")
public class TeamUser {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "team_user_seq")
    @SequenceGenerator(name = "team_user_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @CreationTimestamp
    @Column(name = "join_date", nullable = false)
    private LocalDate joinDate;

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof TeamUser teamUser)) return false;

        return getId().equals(teamUser.getId()) && Objects.equals(getTeam(), teamUser.getTeam())
                && Objects.equals(getUser(), teamUser.getUser())
                && Objects.equals(getGame(), teamUser.getGame())
                && getJoinDate().equals(teamUser.getJoinDate());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + Objects.hashCode(getTeam());
        result = 31 * result + Objects.hashCode(getUser());
        result = 31 * result + Objects.hashCode(getGame());
        result = 31 * result + getJoinDate().hashCode();
        return result;
    }
}