package safety.incidentlens.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "work")
public class Work {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "registration_date", nullable = false)
    private LocalDateTime registrationDate;

    @OneToMany(mappedBy = "work", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Figure> figures = new ArrayList<>();

    @OneToMany(mappedBy = "work", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Connection> connections = new ArrayList<>();

    // Конструкторы
    public Work() {}

    public Work(String title) {
        this.title = title;
        this.registrationDate = LocalDateTime.now();
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public LocalDateTime getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDateTime registrationDate) { this.registrationDate = registrationDate; }

    public List<Figure> getFigures() { return figures; }
    public void setFigures(List<Figure> figures) { this.figures = figures; }

    public List<Connection> getConnections() { return connections; }
    public void setConnections(List<Connection> connections) { this.connections = connections; }

    @Override
    public String toString() {
        return "Work{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", registrationDate=" + registrationDate +
                '}';
    }
}
