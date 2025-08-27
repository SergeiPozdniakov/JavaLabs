package safety.incidentlens.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import safety.incidentlens.model.Connection;

import java.util.List;

public interface ConnectionRepository extends JpaRepository<Connection, Long> {
    List<Connection> findByWorkId(Long workId);
}
