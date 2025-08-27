package safety.incidentlens.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import safety.incidentlens.model.Work;

public interface WorkRepository extends JpaRepository<Work, Long> {
}