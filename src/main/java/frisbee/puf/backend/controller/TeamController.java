package frisbee.puf.backend.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import frisbee.puf.backend.model.Team;
import frisbee.puf.backend.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class TeamController {
    @Autowired
    TeamService teamService;

    @GetMapping("/teams")
    public ResponseEntity<List<Team>> getTeams(){

        List<Team> teamList = this.teamService.getAllTeams();
        HttpStatus httpStatus = teamList.isEmpty()? HttpStatus.NO_CONTENT : HttpStatus.OK;
        return new ResponseEntity<>(teamList, httpStatus);
    }

    @GetMapping("/teams/{name}")
    public ResponseEntity<?> getTeamByName(@PathVariable("name") String name){
        try {
            Team team = this.teamService.getTeamByName(name);
            return new ResponseEntity<>(team, HttpStatus.OK);
        } catch(NoSuchElementException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/teams/player/{email}")
    public ResponseEntity<?> getTeamByPlayer(@PathVariable("email") String email){
        try {
            List<Team> teams = this.teamService.getTeamByPlayer(email);
            return new ResponseEntity<>(teams, HttpStatus.OK);
        } catch(NoSuchElementException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/teams/player/{email}/active")
    public ResponseEntity<?> getActiveTeamByPlayer(@PathVariable("email") String email){
        try {
            List<Team> teams = this.teamService.getActiveTeamByPlayer(email);
            return new ResponseEntity<>(teams, HttpStatus.OK);
        } catch(NoSuchElementException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/teams/create")
    public ResponseEntity<?> createTeam(@RequestBody String name) {
        try {
            Team team = this.teamService.createTeam(name);
            return new ResponseEntity<>(team, HttpStatus.CREATED);
        } catch (IllegalArgumentException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/teams/join")
    public ResponseEntity<?> joinTeam(@RequestBody ObjectNode objectNode) {
        String teamName = objectNode.get("teamName").asText();
        String playerEmail = objectNode.get("playerEmail").asText();

        try {
            Team team = this.teamService.joinTeam(teamName, playerEmail);
            return new ResponseEntity<>(team, HttpStatus.CREATED);
        } catch(Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/teams/update")
    public ResponseEntity<?> updateTeam(@RequestBody ObjectNode objectNode) {
        String name = objectNode.get("name").asText();
        int level = objectNode.get("level").asInt();
        int score = objectNode.get("score").asInt();
        int lives = objectNode.get("lives").asInt();
        boolean active = objectNode.get("active").asBoolean();

        try {
            Team team = this.teamService.updateTeam(name, level, score, lives, active);
            return new ResponseEntity<>(team, HttpStatus.CREATED);
        } catch(NoSuchElementException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
