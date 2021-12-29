package frisbee.puf.backend.controller;

import frisbee.puf.backend.model.Player;
import frisbee.puf.backend.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@CrossOrigin // configure allowed origins
@RestController //bind return values of methods to web response body
@RequestMapping("/api") //declare controller route
public class PlayerController {

    @Autowired // inject PlayerService bean
    PlayerService playerService;

    @RequestMapping("/")
    public String message() {
        return "It's working!";
    }

    @GetMapping("/players")
    public ResponseEntity<List<Player>> getAllPlayers (){

        List<Player> playerList=this.playerService.getAllPlayers();
        HttpStatus httpStatus = playerList.isEmpty()? HttpStatus.NO_CONTENT : HttpStatus.OK;
        return new ResponseEntity<>(playerList, httpStatus);
    }

    @PostMapping("/players/register")
    public ResponseEntity registerPlayer(@RequestBody Player newPlayer) {

        Player registeredPlayer = this.playerService.registerPlayer(newPlayer);
        HttpStatus httpStatus = registeredPlayer == null? HttpStatus.BAD_REQUEST : HttpStatus.CREATED;
        return new ResponseEntity<>(registeredPlayer, httpStatus);
    }

    @PostMapping("/players/login")
    public ResponseEntity loginPlayer(@RequestBody Map<String, String> credentials) {

        Player loggedInPlayer = this.playerService.loginPlayer(credentials);
        HttpStatus httpStatus = loggedInPlayer == null? HttpStatus.BAD_REQUEST : HttpStatus.OK;
        return new ResponseEntity<>(loggedInPlayer, httpStatus);
    }

    @DeleteMapping("/players/delete-all")
    public ResponseEntity deletePlayers() {
        this.playerService.deleteAllPlayers();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/players/update-player-name/{email}")
    public ResponseEntity updatePlayerName(@PathVariable("email") String email, @RequestBody String newName) {

        Player updatedPlayer = this.playerService.updatePlayerName(email, newName);
        HttpStatus httpStatus = updatedPlayer == null? HttpStatus.BAD_REQUEST : HttpStatus.OK;
        return new ResponseEntity<>(updatedPlayer, httpStatus);
    }
}
