package safety.incidentlens.dto;

public class FigureDTO {
    private String tempId;
    private String type;
    private int x;
    private int y;
    private int width;
    private int height;
    private String text;

    // Геттеры и сеттеры
    public String getTempId() { return tempId; }
    public void setTempId(String tempId) { this.tempId = tempId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public int getX() { return x; }
    public void setX(int x) { this.x = x; }

    public int getY() { return y; }
    public void setY(int y) { this.y = y; }

    public int getWidth() { return width; }
    public void setWidth(int width) { this.width = width; }

    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}