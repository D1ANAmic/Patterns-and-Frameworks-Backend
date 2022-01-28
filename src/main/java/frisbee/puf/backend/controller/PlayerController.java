package frisbee.puf.backend.controller;

import frisbee.puf.backend.model.Player;
import frisbee.puf.backend.service.PlayerService;
import io.swagger.v3.oas.annotations.Hidden;
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

    /**
     * The PlayerService instance.
     */
    @Autowired // inject PlayerService bean
            PlayerService playerService;

    /**
     * This method is called on accessing the backend URL.
     *
     * @return a welcome message
     */
    @Hidden
    @RequestMapping("/")
    public String message() {
        return "Welcome to the FRIZZBEE FREAKZ API";
    }

    /**
     * Maps to a GET request on the players endpoint and returns
     * all registered players.
     *
     * @return a ResponseEntity object containing the HTTP status and a list
     * of all Player objects that are stored in the database
     */
    @GetMapping("/players")
    public ResponseEntity<List<Player>> getAllPlayers() {

        List<Player> playerList = this.playerService.getAllPlayers();
        HttpStatus httpStatus = playerList.isEmpty() ? HttpStatus.NO_CONTENT
                : HttpStatus.OK;
        return new ResponseEntity<>(playerList, httpStatus);
    }

    /**
     * Maps to a POST request on the players/register endpoint and passes the
     * submitted Player object to the PlayerService instance in order to save
     * it.
     *
     * @param newPlayer the Player object to be registered
     * @return a ResponseEntity object, containing the HTTP status code and
     * the Player object in case of success, an IllegalArgumentException
     * otherwise
     */
    @PostMapping("/players/register")
    public ResponseEntity registerPlayer(@RequestBody Player newPlayer) {
        try {
            Player registeredPlayer =
                    this.playerService.registerPlayer(newPlayer);
            return new ResponseEntity<>(registeredPlayer, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Maps to a POST request on the players/login endpoint and passes the
     * provided credentials to the PlayerService instance in order to
     * validate them and log in the correspondent player.
     *
     * @param credentials a map of Strings containing a player email and a
     *                    password
     * @return a ResponseEntity object, containing the HTTP status code and
     * the Player object in case of success, an LoginException if the
     * credentials don't match and a NoSuchElementException if no record with
     * the provided email can be found
     */
    @PostMapping("/players/login")
    public ResponseEntity loginPlayer(
            @RequestBody Map<String, String> credentials) {

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

    /**
     * Maps to a DELETE request on the players/delete-all endpoint
     * and notifies the PlayerService instance to delete all players from the
     * database.
     *
     * @return a ResponseEntity object, containing the HTTP status code
     */
    @DeleteMapping("/players/delete-all")
    public ResponseEntity deletePlayers() {
        this.playerService.deleteAllPlayers();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Maps to a PUT request on the players/update-player-name/{email}
     * endpoint and passes a new name to the PlayerService instance in order to
     * update the corresponding player's name.
     *
     * @param email   the player's email
     * @param newName the player's new name
     * @return a ResponseEntity object, containing the HTTP status code and
     * the Player object in case of success, an IllegalArgumentException
     * otherwise
     */
    @PutMapping("/players/update-player-name/{email}")
    public ResponseEntity updatePlayerName(@PathVariable("email") String email,
                                           @RequestBody String newName) {
        try {
            Player updatedPlayer = this.playerService.updatePlayerName(email,
                    newName);
            return new ResponseEntity<>(updatedPlayer, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
