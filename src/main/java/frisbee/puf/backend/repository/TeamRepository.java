package frisbee.puf.backend.repository;

import frisbee.puf.backend.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TeamRepository  extends JpaRepository<Team, Long> {
    @Query("select t from Team t where t.name = ?1")
    List<Team> findByName(String name);
}
