package safety.incidentlens.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import safety.incidentlens.model.Figure;

import java.util.List;

public interface FigureRepository extends JpaRepository<Figure, Long> {
    List<Figure> findByWorkId(Long workId);
}
