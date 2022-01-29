package frisbee.puf.backend.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import frisbee.puf.backend.model.Team;
import frisbee.puf.backend.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.NoSuchElementException;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class TeamController {
    /**
     * The TeamService instance.
     */
    @Autowired
    TeamService teamService;

    /**
     * Maps to a get request on the teams endpoint and returns all existing
     * teams.
     *
     * @return a ResponseEntity object containing the HTTP status and a list
     * of all Team objects that are stored in the database
     */
    @Operation(summary = "Find all teams")
    @GetMapping("/teams")
    public ResponseEntity<List<Team>> getTeams() {

        List<Team> teamList = this.teamService.getAllTeams();
        HttpStatus httpStatus = teamList.isEmpty() ? HttpStatus.NO_CONTENT
                : HttpStatus.OK;
        return new ResponseEntity<>(teamList, httpStatus);
    }

    /**
     * Maps to a GET request on the /teams/{name} endpoint and passes the
     * submitted name String to the TeamService instance in order to get the
     * corresponding Team object.
     *
     * @param name the team's name
     * @return a ResponseEntity object, containing the HTTP status and the Team
     * object in case of success, a NoSuchElementException otherwise
     */
    @Operation(summary = "Find team by name")
    @GetMapping("/teams/{name}")
    @ApiResponse(responseCode = "200", description = "Team found",
            content = @Content(schema = @Schema(implementation = Team.class)))
    @ApiResponse(responseCode = "404", description = "Team not found")
    public ResponseEntity<?> getTeamByName(@PathVariable("name") String name) {
        try {
            String decodedName = URLDecoder.decode(name,
                    StandardCharsets.UTF_8.toString());
            Team team = this.teamService.getTeamByName(decodedName);
            return new ResponseEntity<>(team, HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(),
                    HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Maps to a GET request on the /teams/player/{email} endpoint and passes
     * the submitted player email String to the TeamService instance in order
     * to get all teams this particular player is a member of.
     *
     * @param email the player's email
     * @return a ResponseEntity object, containing the HTTP status and a list
     * of Team objects in case of success, a NoSuchElementException otherwise
     */
    @Operation(summary = "Find teams for player")
    @ApiResponse(responseCode = "200", description = "Team for player found",
            content = @Content(array = @ArraySchema(schema =
            @Schema(implementation = Team.class))))
    @ApiResponse(responseCode = "404", description = "Team not found")
    @GetMapping("/teams/player/{email}")
    public ResponseEntity<?> getTeamByPlayer(
            @PathVariable("email") String email) {
        try {
            List<Team> teams = this.teamService.getTeamByPlayer(email);
            return new ResponseEntity<>(teams, HttpStatus.OK);
        } catch (NoSuchElementException exception) {
            return new ResponseEntity<>(exception.getMessage(),
                    HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Maps to a GET request on the /teams/player/{email}/active endpoint and
     * passes the submitted player email String to the TeamService instance
     * in order to get all active teams this particular player is a member of.
     *
     * @param email the player's email
     * @return a ResponseEntity object, containing the HTTP status and a list
     * of Team objects in case of success, a NoSuchElementException otherwise
     */
    @Operation(summary = "Find active teams for player")
    @ApiResponse(responseCode = "200", description = "Active team for player " +
            "found",
            content = @Content(array = @ArraySchema(schema =
            @Schema(implementation = Team.class))))
    @ApiResponse(responseCode = "404", description = "Team not found")
    @GetMapping("/teams/player/{email}/active")
    public ResponseEntity<?> getActiveTeamByPlayer(
            @PathVariable("email") String email) {
        try {
            List<Team> teams = this.teamService.getActiveTeamByPlayer(email);
            return new ResponseEntity<>(teams, HttpStatus.OK);
        } catch (NoSuchElementException exception) {
            return new ResponseEntity<>(exception.getMessage(),
                    HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Maps to a POST request on the /teams/create endpoint and passes the
     * submitted name String to the TeamService instance in order to create a
     * new team with the provided name.
     *
     * @param name the team's name
     * @return a ResponseEntity object, containing the HTTP status and the
     * team object in case of success, an IllegalArgumentException otherwise
     */
    @Operation(summary = "Create team")
    @ApiResponse(responseCode = "201", description = "Team created",
            content = @Content(schema = @Schema(implementation = Team.class)))
    @ApiResponse(responseCode = "400", description = "Bad request")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Name" +
            " of the team", content = @Content(schema = @Schema(example = "My" +
            " awesome team")))
    @PostMapping("/teams/create")
    public ResponseEntity<?> createTeam(@RequestBody String name) {
        try {
            Team team = this.teamService.createTeam(name);
            return new ResponseEntity<>(team, HttpStatus.CREATED);
        } catch (IllegalArgumentException exception) {
            return new ResponseEntity<>(exception.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Maps to a POST request on the /teams/join endpoint and passes the
     * submitted team name and player email to the TeamService instance in
     * order to add the player to an existing team.
     *
     * @param objectNode contains the team's name and the player's email
     * @return a ResponseEntity object, containing the HTTP status and the
     * team object in case of success, an Exception otherwise
     */
    @Operation(summary = "Join team")
    @ApiResponse(responseCode = "201", description = "Team joined",
            content = @Content(schema = @Schema(implementation = Team.class)))
    @ApiResponse(responseCode = "400", description = "Bad request")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content =
    @Content(schema = @Schema(example = "{\"teamName\": \"My awesome team\", " +
            "\"playerEmail\": \"test@test.de\"}")))
    @PostMapping("/teams/join")
    public ResponseEntity<?> joinTeam(@RequestBody ObjectNode objectNode) {
        String teamName = objectNode.get("teamName").asText();
        String playerEmail = objectNode.get("playerEmail").asText();

        try {
            Team team = this.teamService.joinTeam(teamName, playerEmail);
            return new ResponseEntity<>(team, HttpStatus.CREATED);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Maps to a PUT request on the /teams/update endpoint and passes the
     * submitted team name, level, score, lives and status to the TeamService
     * instance in order to update an existing team.
     *
     * @param objectNode contains the team's name, level, score, lives and
     *                   status
     * @return a ResponseEntity object, containing the HTTP status and the
     * team object in case of success, an Exception otherwise
     */
    @Operation(summary = "Update team")
    @ApiResponse(responseCode = "201", description = "Team updated",
            content = @Content(schema = @Schema(implementation = Team.class)))
    @ApiResponse(responseCode = "404", description = "Team not found")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content =
    @Content(schema = @Schema(example =
            "{\"name\": \"My awesome team\", \"level\": 2, \"score\": 15, " +
                    "\"lives\": 4, \"active\": true}")))
    @PutMapping("/teams/update")
    public ResponseEntity<?> updateTeam(@RequestBody ObjectNode objectNode) {
        String name = objectNode.get("name").asText();
        int level = objectNode.get("level").asInt();
        int score = objectNode.get("score").asInt();
        int lives = objectNode.get("lives").asInt();
        boolean active = objectNode.get("active").asBoolean();

        try {
            Team team = this.teamService.updateTeam(name, level, score, lives,
                    active);
            return new ResponseEntity<>(team, HttpStatus.CREATED);
        } catch (NoSuchElementException exception) {
            return new ResponseEntity<>(exception.getMessage(),
                    HttpStatus.NOT_FOUND);
        }
    }
}
