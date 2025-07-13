package pjatk.s18617.tournamentmanagement.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "announcement")
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "announcement_seq")
    @SequenceGenerator(name = "announcement_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "description", length = 1000)
    private String description;

    @ManyToOne
    @JoinColumn(name = "tournament_id")
    private Tournament tournament;

    @CreationTimestamp
    @Column(name = "creation_date", nullable = false)
    private LocalDate creationDate;

}