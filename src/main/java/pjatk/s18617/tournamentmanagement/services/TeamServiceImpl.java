package pjatk.s18617.tournamentmanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pjatk.s18617.tournamentmanagement.model.Team;
import pjatk.s18617.tournamentmanagement.repositories.TeamRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;

}
