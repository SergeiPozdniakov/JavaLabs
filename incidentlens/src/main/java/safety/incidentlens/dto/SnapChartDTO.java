package safety.incidentlens.dto;

import java.util.List;

public class SnapChartDTO {
    private String title;
    private List<FigureDTO> figures;
    private List<ConnectionDTO> connections;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public List<FigureDTO> getFigures() { return figures; }
    public void setFigures(List<FigureDTO> figures) { this.figures = figures; }

    public List<ConnectionDTO> getConnections() { return connections; }
    public void setConnections(List<ConnectionDTO> connections) { this.connections = connections; }
}