package frisbee.puf.backend.controller;

import frisbee.puf.backend.model.Team;
import frisbee.puf.backend.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    // TODO: team highscore, add team, join team, update team, get one team by name
}
