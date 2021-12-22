package frisbee.puf.backend.repository;

import frisbee.puf.backend.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    // useful JpaRepository methods:
    //save(), findOne(), findById(), findAll(), count(), delete(), deleteById()...

    @Query("select p from Player p where p.email = ?1")
    Player findByEmail(String email);
}
