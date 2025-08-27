package safety.incidentlens.model;

import jakarta.persistence.*;

import jakarta.persistence.*;

@Entity
public class Figure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String type; // CIRCLE, RECTANGLE, ELLIPSE, RHOMBUS

    @Column(nullable = false)
    private int x;

    @Column(nullable = false)
    private int y;

    @Column(nullable = false)
    private int width;

    @Column(nullable = false)
    private int height;

    private String text;

    @ManyToOne
    @JoinColumn(name = "work_id", nullable = false)
    private Work work;

    // Конструкторы
    public Figure() {}

    public Figure(String type, int x, int y, int width, int height, String text, Work work) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.work = work;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public Work getWork() { return work; }
    public void setWork(Work work) { this.work = work; }

    @Override
    public String toString() {
        return "Figure{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                ", text='" + text + '\'' +
                '}';
    }
}
