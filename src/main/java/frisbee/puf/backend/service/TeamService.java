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

    public Team createTeam(String name) {
        List<Team> existingTeam = this.teamRepository.findByName(name);
        if (!existingTeam.isEmpty()) {
            System.out.println("Team already exists!");
            return null;
        }

        // initialize with default values
        Team newTeam = new Team();
        newTeam.setName(name);
        newTeam.setLevel(0);
        newTeam.setLives(5);
        newTeam.setScore(0);
        //save and return
        return this.teamRepository.save(newTeam);
    }
}
