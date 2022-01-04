package frisbee.puf.backend.service;

import frisbee.puf.backend.model.Player;
import frisbee.puf.backend.repository.PlayerRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    public Player getPlayerByEmail(String email) {
        return this.playerRepository.findByEmail(email);
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
