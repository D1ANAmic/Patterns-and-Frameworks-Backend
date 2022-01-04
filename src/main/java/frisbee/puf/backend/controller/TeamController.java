package frisbee.puf.backend.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import frisbee.puf.backend.model.Team;
import frisbee.puf.backend.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public ResponseEntity<Team> getTeamByName(@PathVariable("name") String name){

        Team team = this.teamService.getTeamByName(name);
        HttpStatus httpStatus = team == null ? HttpStatus.NO_CONTENT : HttpStatus.OK;
        return new ResponseEntity<>(team, httpStatus);
    }

    @PostMapping("/teams/create")
    public ResponseEntity<Team> createTeam(@RequestBody String name) {
        Team team = this.teamService.createTeam(name);
        HttpStatus httpStatus = team == null ? HttpStatus.BAD_REQUEST : HttpStatus.CREATED;
        return new ResponseEntity<>(team, httpStatus);
    }

    @PostMapping("/teams/join")
    public ResponseEntity<Team> joinTeam(@RequestBody ObjectNode objectNode) {
        String teamName = objectNode.get("teamName").asText();
        String playerEmail = objectNode.get("playerEmail").asText();

        Team team = this.teamService.joinTeam(teamName, playerEmail);
        HttpStatus httpStatus = team == null ? HttpStatus.BAD_REQUEST : HttpStatus.CREATED;
        return new ResponseEntity<>(team, httpStatus);
    }

    @PutMapping("/teams/update")
    public ResponseEntity<Team> updateTeam(@RequestBody ObjectNode objectNode) {
        String name = objectNode.get("name").asText();
        int level = objectNode.get("level").asInt();
        int score = objectNode.get("score").asInt();
        int lives = objectNode.get("lives").asInt();

        Team team = this.teamService.updateTeam(name, level, score, lives);
        HttpStatus httpStatus = team == null ? HttpStatus.BAD_REQUEST : HttpStatus.CREATED;
        return new ResponseEntity<>(team, httpStatus);
    }

    // TODO: update team
}
