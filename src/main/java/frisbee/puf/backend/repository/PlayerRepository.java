package frisbee.puf.backend.repository;

import frisbee.puf.backend.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    // useful JpaRepository methods:
    //save(), findOne(), findById(), findAll(), count(), delete(), deleteById()...
}
