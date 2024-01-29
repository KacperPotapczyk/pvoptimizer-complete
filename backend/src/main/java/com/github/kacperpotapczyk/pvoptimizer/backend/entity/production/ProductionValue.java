package com.github.kacperpotapczyk.pvoptimizer.backend.entity.production;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "production_value",
        indexes = {@Index(name = "idx_production_value_revision_id", columnList = "revision_id")}
)
public class ProductionValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "revision_id")
    private ProductionRevision productionRevision;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @Column(name = "production", nullable = false)
    private double value;

    public ProductionValue(double value, LocalDateTime dateTime) {
        this.value = value;
        this.dateTime = dateTime;
    }

    public boolean isActiveInTimeWindow(LocalDateTime windowStart, LocalDateTime windowEnd) {

        return isBetweenClosedRange(dateTime, windowStart, windowEnd);
    }

    private boolean isBetweenClosedRange(LocalDateTime dateTime, LocalDateTime windowStart, LocalDateTime windowEnd) {

        return (windowStart.isBefore(dateTime) && dateTime.isBefore(windowEnd)) || dateTime.isEqual(windowStart) || dateTime.isEqual(windowEnd);
    }
}