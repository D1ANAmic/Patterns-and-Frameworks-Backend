package frisbee.puf.backend.controller;

import frisbee.puf.backend.model.Player;
import frisbee.puf.backend.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.LoginException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@CrossOrigin // configure allowed origins
@RestController //bind return values of methods to web response body
@RequestMapping("/api") //declare controller route
public class PlayerController {

    @Autowired // inject PlayerService bean
    PlayerService playerService;

    @RequestMapping("/")
    public String message() {
        return "Welcome to the Frizzbee Freakz API";
    }

    @GetMapping("/players")
    public ResponseEntity<List<Player>> getAllPlayers (){

        List<Player> playerList=this.playerService.getAllPlayers();
        HttpStatus httpStatus = playerList.isEmpty()? HttpStatus.NO_CONTENT : HttpStatus.OK;
        return new ResponseEntity<>(playerList, httpStatus);
    }

    @PostMapping("/players/register")
    public ResponseEntity registerPlayer(@RequestBody Player newPlayer) {
        try {
            Player registeredPlayer = this.playerService.registerPlayer(newPlayer);
            return new ResponseEntity<>(registeredPlayer, HttpStatus.CREATED);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/players/login")
    public ResponseEntity loginPlayer(@RequestBody Map<String, String> credentials) {

        Player loggedInPlayer = null;
        try {
            loggedInPlayer = this.playerService.loginPlayer(credentials);
            return new ResponseEntity<>(loggedInPlayer, HttpStatus.OK);
        } catch (LoginException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/players/delete-all")
    public ResponseEntity deletePlayers() {
        this.playerService.deleteAllPlayers();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/players/update-player-name/{email}")
    public ResponseEntity updatePlayerName(@PathVariable("email") String email, @RequestBody String newName) {
        try {
            Player updatedPlayer = this.playerService.updatePlayerName(email, newName);
            return new ResponseEntity<>(updatedPlayer, HttpStatus.OK);
        } catch (IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }
}
