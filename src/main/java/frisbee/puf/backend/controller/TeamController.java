package frisbee.puf.backend.controller;

import frisbee.puf.backend.model.Team;
import frisbee.puf.backend.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class TeamController {
    @Autowired
    TeamService teamService;

    @GetMapping("/teams")
    public ResponseEntity<List<Team>> getTeams(){

        List<Team> teamList=this.teamService.getAllTeams();
        HttpStatus httpStatus = teamList.isEmpty()? HttpStatus.NO_CONTENT : HttpStatus.OK;
        return new ResponseEntity<>(teamList, httpStatus);
    }

    @PostMapping("/teams/create")
    public ResponseEntity createTeam(@RequestBody String name) {
        Team team = this.teamService.createTeam(name);
        HttpStatus httpStatus = team == null ? HttpStatus.BAD_REQUEST : HttpStatus.CREATED;
        return new ResponseEntity<>(team, httpStatus);
    }

    // TODO: team highscore, join team, update team, get one team by name
}
