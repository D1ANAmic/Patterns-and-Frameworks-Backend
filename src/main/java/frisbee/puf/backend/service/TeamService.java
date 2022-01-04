package frisbee.puf.backend.service;

import frisbee.puf.backend.model.Player;
import frisbee.puf.backend.model.Team;
import frisbee.puf.backend.repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService {
    TeamRepository teamRepository;
    PlayerService playerService;

    public TeamService(TeamRepository teamRepository, PlayerService playerService) {
        this.teamRepository = teamRepository;
        this.playerService = playerService;
    }

    public List<Team> getAllTeams(){
        return this.teamRepository.findAll();
    }

    public Team getTeamByName(String name){
        List<Team> teams = this.teamRepository.findByName(name);
        if (teams.isEmpty()) {
            System.out.println("Team does not exist.");
            return null;
        }

        // return first team found, since there should not be more than one with the same name
        return teams.get(0);
    }

    public Team createTeam(String name) {
        List<Team> existingTeams = this.teamRepository.findByName(name);
        if (!existingTeams.isEmpty()) {
            System.out.println("Team already exists.");
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

    public Team joinTeam(String teamName, String playerEmail) {
        Team team = this.getTeamByName(teamName);
        if (team == null) {
            System.out.println("Team does not exist.");
            return null;
        }

        Player player = this.playerService.getPlayerByEmail(playerEmail);
        if (player == null) {
            System.out.println("Player does not exist.");
            return null;
        }

        if ((team.getPlayerLeft() != null && team.getPlayerLeft().equals(player)) ||
                (team.getPlayerRight() != null && team.getPlayerRight().equals(player))) {
            System.out.println("Player already in team.");
            return null;
        }

        // fill up places
        if (team.getPlayerLeft() == null) {
            team.setPlayerLeft(player);
            return this.teamRepository.save(team);
        } else if (team.getPlayerRight() == null) {
            team.setPlayerRight(player);
            return this.teamRepository.save(team);
        } else {
            // all places already taken
            System.out.println("Team already full.");
            return null;
        }
    }

    public Team updateTeam(String name, int level, int score, int lives) {
        Team team = this.getTeamByName(name);
        if (team == null) {
            System.out.println("Team does not exist.");
            return null;
        }

        team.setLevel(level);
        team.setScore(score);
        team.setLives(lives);
        return this.teamRepository.save(team);
    }
}
