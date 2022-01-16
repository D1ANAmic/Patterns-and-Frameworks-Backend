package frisbee.puf.backend.service;

import frisbee.puf.backend.model.Player;
import frisbee.puf.backend.repository.PlayerRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

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

    public Player registerPlayer(Player newPlayer){
        List<Player> players = this.playerRepository.findAll();

        for (Player player : players) {
            if (player.getEmail().equals(newPlayer.getEmail())) {
                log.info("Player already exists!");
                return null;
            }
        }
        newPlayer.setPassword(this.passwordEncoder.encode(newPlayer.getPassword()));
        return this.playerRepository.save(newPlayer);
    }

    public Player loginPlayer(Map<String, String> credentials){
        Player player = playerRepository.findByEmail(credentials.get("email"));
        if (player == null){
            log.info("Player with given email doesn't exist!");
            return null;
        }
        if (passwordEncoder.matches(credentials.get("password"), player.getPassword())){
            return player;
        } else{
            log.info("Login failed!");
            return null;
        }
    }

    public void deleteAllPlayers(){
        this.playerRepository.deleteAll();
        log.info("All players deleted!");
    }

    public Player updatePlayerName(String email, String newName) {
        Player currentPlayer = playerRepository.findByEmail(email);
        log.info("CURRENT PLAYERS NAME: " + currentPlayer.getName());
        if (newName != null && newName.length() > 0 && !Objects.equals(currentPlayer.getName(), newName)) {
            currentPlayer.setName(newName);
            return this.playerRepository.save(currentPlayer);
        } else {
            log.info("Name can not be modified!");
            return null;
        }
    }

}
