package frisbee.puf.backend.service;

import frisbee.puf.backend.model.Player;
import frisbee.puf.backend.repository.PlayerRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

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
     * Gets a player object by id and returns it.
     * @param id id of the player
     * @return player object
     * @throws java.util.NoSuchElementException if player does not exist
     */
    public Optional<Player> getPlayerById(long id) throws NoSuchElementException {
        Optional<Player> player = this.playerRepository.findById(id);
        if (player.isEmpty()) {
            throw new NoSuchElementException("Player with this id does not exist.");
        }

        return player;
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
                System.out.println("Player already exists!");
                return null;
            }
        }
        newPlayer.setPassword(this.passwordEncoder.encode(newPlayer.getPassword()));
        return this.playerRepository.save(newPlayer);
    }

    public Player loginPlayer(Map<String, String> credentials){
        Player player = playerRepository.findByEmail(credentials.get("email"));
        if (player == null){
            System.out.println("Player with given email doesn't exist!");
            return null;
        }
        if (passwordEncoder.matches(credentials.get("password"), player.getPassword())){
            return player;
        } else{
            System.out.println("Login failed!");
            return null;
        }
    }

    public void deleteAllPlayers(){
        this.playerRepository.deleteAll();
        System.out.println("All players deleted!");
    }

    public Player updatePlayerName(String email, String newName) {
        Player currentPlayer = playerRepository.findByEmail(email);
        System.out.println("CURRENT PLAYERS NAME: " + currentPlayer.getName());
        if (newName != null && newName.length() > 0 && !Objects.equals(currentPlayer.getName(), newName)) {
            currentPlayer.setName(newName);
            return this.playerRepository.save(currentPlayer);
        } else {
            System.out.println("Name can not be modified!");
            return null;
        }
    }

}
