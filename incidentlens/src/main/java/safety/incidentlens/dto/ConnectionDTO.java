package safety.incidentlens.dto;

public class ConnectionDTO {
    private String sourceTempId;
    private String targetTempId;

    public String getSourceTempId() { return sourceTempId; }
    public void setSourceTempId(String sourceTempId) { this.sourceTempId = sourceTempId; }

    public String getTargetTempId() { return targetTempId; }
    public void setTargetTempId(String targetTempId) { this.targetTempId = targetTempId; }
}
