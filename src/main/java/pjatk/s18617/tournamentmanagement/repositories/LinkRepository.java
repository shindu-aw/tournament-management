package pjatk.s18617.tournamentmanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pjatk.s18617.tournamentmanagement.model.Link;

public interface LinkRepository extends JpaRepository<Link, Long> {
}