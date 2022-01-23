package frisbee.puf.backend.repository;

import frisbee.puf.backend.model.Player;
import frisbee.puf.backend.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {
    /**
     * Selects a Team record based on the provided email from
     * the database.
     *
     * @param name the team's name
     * @return a team object
     */
    @Query("select t from Team t where t.name = ?1")
    List<Team> findByName(String name);

    /**
     * Selects a Team record based on the provided player from
     * the database.
     *
     * @param player the player
     * @return a team object
     */
    @Query("select t from Team t where t.playerLeft = ?1 or t.playerRight = ?1")
    List<Team> findByPlayer(Player player);

    /**
     * Selects a Team record based on the provided player and the status
     * active from the database.
     *
     * @param player the player
     * @return a team object
     */
    @Query("select t from Team t where (t.playerLeft = ?1 or t.playerRight = "
            + "?1) and t.active = true")
    List<Team> findActiveByPlayer(Player player);
}
