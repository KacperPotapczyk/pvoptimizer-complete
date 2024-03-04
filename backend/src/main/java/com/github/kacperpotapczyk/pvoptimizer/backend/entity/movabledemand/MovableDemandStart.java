package com.github.kacperpotapczyk.pvoptimizer.backend.entity.movabledemand;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(
        name = "movable_demand_start",
        indexes = {@Index(name = "idx_movable_demand_start_revision_id", columnList = "revision_id")}
)
public class MovableDemandStart implements Comparable<MovableDemandStart> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "revision_id", nullable = false)
    private MovableDemandRevision movableDemandRevision;

    @Column(name = "start", nullable = false)
    private LocalDateTime start;

    public MovableDemandStart(LocalDateTime start) {
        this.start = start;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MovableDemandStart that)) return false;

        return getStart().equals(that.getStart());
    }

    @Override
    public int hashCode() {
        return getStart().hashCode();
    }

    @Override
    public int compareTo(MovableDemandStart movableDemandStart) {
        return start.compareTo(movableDemandStart.getStart());
    }
}