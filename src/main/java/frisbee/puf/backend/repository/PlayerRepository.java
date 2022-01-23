package frisbee.puf.backend.repository;

import frisbee.puf.backend.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    /**
     * Selects a Player record based on the provided email from
     * the database.
     *
     * @param email the player's email
     * @return a Player object
     */
    @Query("select p from Player p where p.email = ?1")
    Player findByEmail(String email);
}
