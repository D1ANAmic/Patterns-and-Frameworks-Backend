package frisbee.puf.backend.controller;

import frisbee.puf.backend.model.Player;
import frisbee.puf.backend.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@CrossOrigin // configure allowed origins
@RestController //bind return values of methods to web response body
@RequestMapping("/api") //declare controller route
public class PlayerController {

    @Autowired // inject PlayerRepository bean
    PlayerRepository playerRepository;

    @RequestMapping("/")
    public String message() {
        return "It's working!";
    }

    @GetMapping("/players")
    public ResponseEntity<List<Player>> getAllPlayers (@RequestParam(required = false) String name){
        try {
            List<Player> allPlayers = this.playerRepository.findAll();
            if (allPlayers.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(allPlayers, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/players/{id}")
    public ResponseEntity<Player> getTutorialById(@PathVariable("id") long id) {
        Optional<Player> playerData = playerRepository.findById(id);

        if (playerData.isPresent()) {
            return new ResponseEntity<>(playerData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/players/register")
    public ResponseEntity<Player> registerPlayer(@RequestBody Player newPlayer) {
        List<Player> players = playerRepository.findAll();

        for (Player player : players) {
            System.out.println("Registered player: " + player.toString());

            if (player.getEmail().equals(newPlayer.getEmail())) {
                System.out.println("Player already exists!");
                return new ResponseEntity<>(newPlayer, HttpStatus.BAD_REQUEST);
            }
        }

        playerRepository.save(newPlayer);
        return new ResponseEntity<>(newPlayer, HttpStatus.CREATED);
    }

    @PostMapping("/players/login")
    public ResponseEntity<Player>  loginPlayer(@RequestBody Player player) {
        List<Player> players = playerRepository.findAll();

        for (Player other : players) {
            if (other.getEmail().equals(player.getEmail()) && other.getPassword().equals(player.getPassword())) {
                other.setLoggedIn(true);
                playerRepository.save(other);
                return new ResponseEntity<>(other, HttpStatus.OK);
            }
        }
        System.out.println("Login failed!");
        return new ResponseEntity<>(player, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/players/logout")
    public ResponseEntity<Player> logoutPlayer(@RequestBody Player player) {
        List<Player> players = playerRepository.findAll();

        for (Player other : players) {
            if (other.getEmail().equals(player.getEmail())) {
                other.setLoggedIn(false);
                playerRepository.save(other);
                return new ResponseEntity<>(other, HttpStatus.OK);
            }
        }

        System.out.println("Logout failed!");
        return new ResponseEntity<>(player, HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/players/delete-all")
    public ResponseEntity<Player> deletePlayers() {
        playerRepository.deleteAll();
        System.out.println("All players deleted!");
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
