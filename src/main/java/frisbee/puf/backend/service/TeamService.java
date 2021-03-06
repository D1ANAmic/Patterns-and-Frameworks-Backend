package frisbee.puf.backend.service;

import frisbee.puf.backend.model.Player;
import frisbee.puf.backend.model.Team;
import frisbee.puf.backend.repository.TeamRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
public class TeamService {
    /**
     * The TeamRepository instance that handles the database access.
     */
    TeamRepository teamRepository;

    /**
     * The PlayerService instance.
     */
    PlayerService playerService;

    /**
     * Constructs a TeamService instance and passes the referenced
     * TeamRepository and PlayerService.
     *
     * @param teamRepository
     * @param playerService
     */
    public TeamService(TeamRepository teamRepository,
                       PlayerService playerService) {
        this.teamRepository = teamRepository;
        this.playerService = playerService;
    }

    /**
     * Gets a list of all Team objects stored in the database.
     *
     * @return list of Team objects
     */
    public List<Team> getAllTeams() {
        return this.teamRepository.findAll();
    }


    /**
     * Gets a Team object by name and returns it.
     *
     * @param name of the team
     * @return Team object
     * @throws NoSuchElementException if team doesn't exist
     */
    public Team getTeamByName(String name) throws NoSuchElementException {
        List<Team> teams = this.teamRepository.findByName(name);
        if (teams.isEmpty()) {
            log.info("Team does not exist.");
            throw new NoSuchElementException("Team does not exist.");
        }

        // return first team found, since there should not be more than one
        // with the same name
        return teams.get(0);
    }

    /**
     * Gets a Team object by player's email and returns it.
     *
     * @param email email of the player
     * @return Team object
     * @throws NoSuchElementException if team doesn't exist
     */
    public List<Team> getTeamByPlayer(
            String email) throws NoSuchElementException {
        Player player = this.playerService.getPlayerByEmail(email);
        if (player == null) {
            log.info("Player does not exist.");
            throw new NoSuchElementException("Player does not exist.");
        }

        List<Team> teams = this.teamRepository.findByPlayer(player);
        if (teams.isEmpty()) {
            log.info("Team does not exist.");
            throw new NoSuchElementException("Team does not exist.");
        }

        // return all teams for player
        return teams;
    }

    /**
     * Gets a list of active teams by player email and returns it.
     *
     * @param email email of the player
     * @return list of Team objects
     * @throws NoSuchElementException if no player with the provided
     *                                email or no active team exists
     */
    public List<Team> getActiveTeamByPlayer(
            String email) throws NoSuchElementException {
        Player player = this.playerService.getPlayerByEmail(email);
        if (player == null) {
            log.info("Player does not exist.");
            throw new NoSuchElementException("Player does not exist.");
        }

        List<Team> teams = this.teamRepository.findActiveByPlayer(player);
        if (teams.isEmpty()) {
            log.info("No active team exists.");
            throw new NoSuchElementException("No active team exists.");
        }

        // return all teams for player
        return teams;
    }

    /**
     * Creates a new team and returns it.
     *
     * @param name name of the team
     * @return created Team object
     * @throws IllegalArgumentException if team name already exists
     */
    public Team createTeam(String name) throws IllegalArgumentException {
        List<Team> existingTeams = this.teamRepository.findByName(name);
        if (!existingTeams.isEmpty()) {
            log.info("Team already exists.");
            throw new IllegalArgumentException("Team already exists.");
        }

        // initialize with default values
        Team newTeam = new Team();
        newTeam.setName(name);
        newTeam.setLevel(1);
        newTeam.setLives(5);
        newTeam.setScore(0);
        newTeam.setActive(true);

        // save and return
        return this.teamRepository.save(newTeam);
    }

    /**
     * Adds a player to a team.
     *
     * @param teamName    name of the team
     * @param playerEmail email of the player
     * @return the updated Team object
     * @throws NoSuchElementException   if the team or player do not exist
     * @throws IllegalArgumentException if the player is already in the team
     * or the team is full
     */
    public Team joinTeam(String teamName,
                         String playerEmail) throws NoSuchElementException,
            IllegalArgumentException {
        Team team = this.getTeamByName(teamName);
        Player player = this.playerService.getPlayerByEmail(playerEmail);
        if ((team.getPlayerLeft() != null && team.getPlayerLeft()
                .equals(player)) || (team.getPlayerRight() != null
                && team.getPlayerRight().equals(player))) {
            log.info("Player already in team.");
            throw new IllegalArgumentException("Player is already in the team"
                    + ".");
        }

        // fill up places
        if (team.getPlayerLeft() == null) {
            team.setPlayerLeft(player);
            return this.teamRepository.save(team);
        } else if (team.getPlayerRight() == null) {
            team.setPlayerRight(player);
            return this.teamRepository.save(team);
        } else {
            // all places are already taken
            log.info("Team already full.");
            throw new IllegalArgumentException("Team is already full.");
        }
    }

    /**
     * Updates a team with given parameters.
     *
     * @param name   name of the team
     * @param level  new level of the team
     * @param score  new score of the team
     * @param lives  new remaining lives of the team
     * @param active active status of the team
     * @return the updated Team object
     * @throws NoSuchElementException if team was not found
     */
    public Team updateTeam(String name, int level, int score, int lives,
                           boolean active) throws NoSuchElementException {
        Team team = this.getTeamByName(name);

        team.setLevel(level);
        team.setScore(score);
        team.setLives(lives);
        team.setActive(active);
        return this.teamRepository.save(team);
    }
}
