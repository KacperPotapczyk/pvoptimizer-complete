package com.github.kacperpotapczyk.pvoptimizer.backend.entity.demand;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "demand_value",
        indexes = {@Index(name = "idx_demand_value_revision_id", columnList = "revision_id")})
public class DemandValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @ManyToOne
    @JoinColumn(name = "revision_id")
    private DemandRevision demandRevision;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @Column(name = "demand", nullable = false)
    private double value;

    public DemandValue(double value, LocalDateTime dateTime) {
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
