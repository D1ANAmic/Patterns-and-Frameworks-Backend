package frisbee.puf.backend.service;

import frisbee.puf.backend.model.Team;
import frisbee.puf.backend.repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService {
    TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public List<Team> getAllTeams(){
        return this.teamRepository.findAll();
    }
}
