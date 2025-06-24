package pjatk.s18617.tournamentmanagement.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "announcement")
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "announcement_seq")
    @SequenceGenerator(name = "announcement_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "description", length = 1000)
    private String description; // bidirectional TODO maintain relationships

    @ManyToOne
    @JoinColumn(name = "tournament_id")
    private Tournament tournament; // bidirectional TODO maintain relationships

}