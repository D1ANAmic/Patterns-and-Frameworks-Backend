package frisbee.puf.backend.service;

import frisbee.puf.backend.model.Player;
import frisbee.puf.backend.model.Team;
import frisbee.puf.backend.repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TeamService {
    TeamRepository teamRepository;
    PlayerService playerService;

    public TeamService(TeamRepository teamRepository, PlayerService playerService) {
        this.teamRepository = teamRepository;
        this.playerService = playerService;
    }

    /**
     * Returns all teams saved in the database.
     * @return list of all teams
     */
    public List<Team> getAllTeams(){
        return this.teamRepository.findAll();
    }

    /**
     * Gets a team by name and returns it.
     * @param name of the team
     * @return Team object
     * @throws NoSuchElementException when team is not found
     */
    public Team getTeamByName(String name) throws NoSuchElementException {
        List<Team> teams = this.teamRepository.findByName(name);
        if (teams.isEmpty()) {
            System.out.println("Team does not exist.");
            throw new NoSuchElementException("Team does not exist.");
        }

        // return first team found, since there should not be more than one with the same name
        return teams.get(0);
    }

    /**
     * Creates a new team and returns it.
     * @param name name of team
     * @return created team object
     * @throws IllegalArgumentException if team name already exists
     */
    public Team createTeam(String name) throws IllegalArgumentException {
        List<Team> existingTeams = this.teamRepository.findByName(name);
        if (!existingTeams.isEmpty()) {
            System.out.println("Team already exists.");
            throw new IllegalArgumentException("Team already exists.");
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

    /**
     * Adds a player to a team.
     * @param teamName name of the team
     * @param playerEmail email adress of the player
     * @return the updated team object
     * @throws NoSuchElementException if the team or player do not exist
     * @throws IllegalArgumentException if the player is already in the team or the team is full
     */
    public Team joinTeam(String teamName, String playerEmail) throws NoSuchElementException, IllegalArgumentException {
        Team team = this.getTeamByName(teamName);
        Player player = this.playerService.getPlayerByEmail(playerEmail);

        if ((team.getPlayerLeft() != null && team.getPlayerLeft().equals(player)) ||
                (team.getPlayerRight() != null && team.getPlayerRight().equals(player))) {
            System.out.println("Player already in team.");
            throw new IllegalArgumentException("Player is already in the team.");
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
            throw new IllegalArgumentException("Team is already full.");
        }
    }

    /**
     * Updates a team with given parameters.
     * @param name name of the team
     * @param level new level of the team
     * @param score new score of the team
     * @param lives new remaining lives of the team
     * @return the updated team object
     * @throws NoSuchElementException if team was not found
     */
    public Team updateTeam(String name, int level, int score, int lives) throws NoSuchElementException {
        Team team = this.getTeamByName(name);

        team.setLevel(level);
        team.setScore(score);
        team.setLives(lives);
        return this.teamRepository.save(team);
    }
}
