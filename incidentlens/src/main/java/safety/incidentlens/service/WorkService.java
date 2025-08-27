package safety.incidentlens.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import safety.incidentlens.dto.ConnectionDTO;
import safety.incidentlens.dto.FigureDTO;
import safety.incidentlens.dto.SnapChartDTO;
import safety.incidentlens.model.Connection;
import safety.incidentlens.model.Figure;
import safety.incidentlens.model.Work;
import safety.incidentlens.repository.ConnectionRepository;
import safety.incidentlens.repository.FigureRepository;
import safety.incidentlens.repository.WorkRepository;

import java.util.*;

@Service
public class WorkService {

    private final WorkRepository workRepository;
    private final FigureRepository figureRepository;
    private final ConnectionRepository connectionRepository;

    public WorkService(WorkRepository workRepository,
                       FigureRepository figureRepository,
                       ConnectionRepository connectionRepository) {
        this.workRepository = workRepository;
        this.figureRepository = figureRepository;
        this.connectionRepository = connectionRepository;
    }

    @Transactional
    public Work saveSnapChart(SnapChartDTO dto) {
        Work work = new Work(dto.getTitle() != null ? dto.getTitle() : "Untitled Snap Chart");
        work = workRepository.save(work);

        Map<String, Figure> figureMap = new HashMap<>();
        if (dto.getFigures() != null) {
            for (FigureDTO figureDTO : dto.getFigures()) {
                Figure figure = new Figure(
                        figureDTO.getType(),
                        figureDTO.getX(),
                        figureDTO.getY(),
                        figureDTO.getWidth(),
                        figureDTO.getHeight(),
                        figureDTO.getText(),
                        work
                );
                Figure savedFigure = figureRepository.save(figure);
                figureMap.put(figureDTO.getTempId(), savedFigure);
            }
        }

        if (dto.getConnections() != null) {
            for (ConnectionDTO connDTO : dto.getConnections()) {
                Figure source = figureMap.get(connDTO.getSourceTempId());
                Figure target = figureMap.get(connDTO.getTargetTempId());
                if (source != null && target != null) {
                    Connection connection = new Connection(source, target, work);
                    connectionRepository.save(connection);
                }
            }
        }

        return work;
    }

    public List<Work> getAllWorks() {
        return workRepository.findAll();
    }

    public Work getWorkWithDetails(Long id) {
        Work work = workRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Work not found"));

        work.setFigures(figureRepository.findByWorkId(id));
        work.setConnections(connectionRepository.findByWorkId(id));

        return work;
    }
}