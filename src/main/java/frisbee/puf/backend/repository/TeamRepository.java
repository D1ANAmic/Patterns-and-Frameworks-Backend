package frisbee.puf.backend.repository;

import frisbee.puf.backend.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository  extends JpaRepository<Team, Long> {
}
