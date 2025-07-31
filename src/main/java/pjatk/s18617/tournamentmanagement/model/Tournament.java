package pjatk.s18617.tournamentmanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Builder.Default
    @Column(name = "finished", nullable = false)
    private Boolean finished = false;

    @Size(min = 8, max = 8)
    @Column(name = "join_secret_code", nullable = false, length = 8)
    private String joinSecretCode; // for joining teams

    @Size(min = 8, max = 8)
    @Column(name = "manage_secret_code", nullable = false, length = 8)
    private String manageSecretCode; // for user moderating tournament

    @ManyToOne
    @JoinColumn(name = "user_owner_id", nullable = false)
    private User userOwner;

    @Builder.Default
    @ManyToMany
    @JoinTable(name = "tournament_user",
            joinColumns = @JoinColumn(name = "tournament_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @OrderBy("username ASC")
    private List<User> usersManaging = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "location_id")
    private Location location;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @Builder.Default
    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("team ASC, id ASC")
    private List<TournamentTeam> teamRegistrations = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("date DESC, id DESC")
    private List<Match> matches = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("creationDate DESC")
    private List<Announcement> announcements = new ArrayList<>();

    public boolean isManagedByUser(String username) {
        return usersManaging.stream().anyMatch(user -> user.getUsername().equals(username));
    }


    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Tournament that)) return false;

        return getId().equals(that.getId())
                && getName().equals(that.getName())
                && Objects.equals(getDescription(), that.getDescription())
                && getStartDate().equals(that.getStartDate())
                && Objects.equals(getEndDate(), that.getEndDate())
                && getFinished().equals(that.getFinished())
                && getJoinSecretCode().equals(that.getJoinSecretCode())
                && getManageSecretCode().equals(that.getManageSecretCode())
                && getUserOwner().equals(that.getUserOwner())
                && Objects.equals(getLocation(), that.getLocation())
                && Objects.equals(getGame(), that.getGame());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getName().hashCode();
        result = 31 * result + Objects.hashCode(getDescription());
        result = 31 * result + getStartDate().hashCode();
        result = 31 * result + Objects.hashCode(getEndDate());
        result = 31 * result + getFinished().hashCode();
        result = 31 * result + getJoinSecretCode().hashCode();
        result = 31 * result + getManageSecretCode().hashCode();
        result = 31 * result + getUserOwner().hashCode();
        result = 31 * result + Objects.hashCode(getLocation());
        result = 31 * result + Objects.hashCode(getGame());
        return result;
    }

}