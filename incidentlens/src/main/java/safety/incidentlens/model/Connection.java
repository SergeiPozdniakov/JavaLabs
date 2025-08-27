package safety.incidentlens.model;

import jakarta.persistence.*;

import jakarta.persistence.*;

@Entity
public class Connection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "source_figure_id", nullable = false)
    private Figure source;

    @ManyToOne
    @JoinColumn(name = "target_figure_id", nullable = false)
    private Figure target;

    @ManyToOne
    @JoinColumn(name = "work_id", nullable = false)
    private Work work;

    // Конструкторы
    public Connection() {}

    public Connection(Figure source, Figure target, Work work) {
        this.source = source;
        this.target = target;
        this.work = work;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Figure getSource() { return source; }
    public void setSource(Figure source) { this.source = source; }

    public Figure getTarget() { return target; }
    public void setTarget(Figure target) { this.target = target; }

    public Work getWork() { return work; }
    public void setWork(Work work) { this.work = work; }

    @Override
    public String toString() {
        return "Connection{" +
                "id=" + id +
                ", sourceId=" + (source != null ? source.getId() : null) +
                ", targetId=" + (target != null ? target.getId() : null) +
                '}';
    }
}
