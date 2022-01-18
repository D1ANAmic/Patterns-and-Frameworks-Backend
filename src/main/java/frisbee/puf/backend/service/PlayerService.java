package frisbee.puf.backend.service;

import frisbee.puf.backend.model.Player;
import frisbee.puf.backend.repository.PlayerRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import javax.security.auth.login.LoginException;
import java.util.*;

@Slf4j
@Service
public class PlayerService {

    PlayerRepository playerRepository;
    BCryptPasswordEncoder passwordEncoder;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Gets a list of all players and returns it
     * @return list of player objects
     */
    public List<Player> getAllPlayers(){
        return this.playerRepository.findAll();
    }


    /**
     * Gets a player object by email and returns it.
     * @param email email adress of the player
     * @return player object
     * @throws java.util.NoSuchElementException if player does not exist
     */
    public Player getPlayerByEmail(String email) throws NoSuchElementException {
        Player player = this.playerRepository.findByEmail(email);
        if (player == null) {
            throw new NoSuchElementException("Player with this email does not exist.");
        }
        return player;
    }

    /**
     * Saves a new player object in database and returns it
     * @param newPlayer player object to be saved
     * @return player object
     * @throws IllegalArgumentException if player with same email already exists
     */
    public Player registerPlayer(Player newPlayer) throws IllegalArgumentException{
        List<Player> players = this.playerRepository.findAll();

        for (Player player : players) {
            if (player.getEmail().equals(newPlayer.getEmail())) {
                log.info("Player already exists!");
                throw new IllegalArgumentException("Player already exists!");
            }
        }
        newPlayer.setPassword(this.passwordEncoder.encode(newPlayer.getPassword()));
        return this.playerRepository.save(newPlayer);
    }

    /**
     * Logs in player based on credentials and returns logged in player
     * @param credentials email and password of player to be logged in
     * @return logged in player object
     * @throws NoSuchElementException if player with given email doesn't exist
     * @throws LoginException if password is incorrect
     */
    public Player loginPlayer(Map<String, String> credentials) throws NoSuchElementException, LoginException {
        Player player = playerRepository.findByEmail(credentials.get("email"));
        if (player == null){
            log.info("Player with given email doesn't exist!");
            throw new NoSuchElementException("Player with given email doesn't exist!");
        }
        if (passwordEncoder.matches(credentials.get("password"), player.getPassword())){
            return player;
        } else{
            log.info("Login failed!");
            throw new LoginException("Login failed!");
        }
    }

    /**
     * Deletes all players from database
     */
    public void deleteAllPlayers(){
        this.playerRepository.deleteAll();
        log.info("All players deleted!");
    }

    /**
     * Updates player name and returns player object
     * @param email email of player by which player object is identified
     * @param newName new name for player object
     * @return player object with updated name
     * @throws IllegalArgumentException if new name is empty or player with new name already exists
     */
    public Player updatePlayerName(String email, String newName) throws IllegalArgumentException{
        Player currentPlayer = playerRepository.findByEmail(email);
        log.info("CURRENT PLAYERS NAME: " + currentPlayer.getName());
        if (newName != null && newName.length() > 0 && !Objects.equals(currentPlayer.getName(), newName)) {
            currentPlayer.setName(newName);
            return this.playerRepository.save(currentPlayer);
        } else {
            log.info("Name can not be modified!");
            throw new IllegalArgumentException("Name can not be modified!");
        }
    }

}
