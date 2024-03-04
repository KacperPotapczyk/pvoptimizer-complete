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
        name = "movable_demand_value",
        indexes = {@Index(name = "idx_movable_demand_value_revision_id", columnList = "revision_id")}
)
public class MovableDemandValue implements Comparable<MovableDemandValue> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "revision_id", nullable = false)
    private MovableDemandRevision movableDemandRevision;

    @Column(name = "value_order", nullable = false)
    private Long order;

    @Column(name = "duration", nullable = false)
    private long durationMinutes;

    @Column(name = "demand", nullable = false)
    private double value;

    public MovableDemandValue(long order, double value, long durationMinutes) {
        this.order = order;
        this.value = value;
        this.durationMinutes = durationMinutes;
    }

    public boolean isActiveInTimeWindow(LocalDateTime demandStart ,LocalDateTime windowStart, LocalDateTime windowEnd) {

        return isBetweenClosedRange(demandStart.plusMinutes(durationMinutes), windowStart, windowEnd);
    }

    private boolean isBetweenClosedRange(LocalDateTime dateTime, LocalDateTime windowStart, LocalDateTime windowEnd) {

        return (windowStart.isBefore(dateTime) && dateTime.isBefore(windowEnd)) || dateTime.isEqual(windowStart) || dateTime.isEqual(windowEnd);
    }

    @Override
    public int compareTo(MovableDemandValue movableDemandValue) {
        return order.compareTo(movableDemandValue.getOrder());
    }
}