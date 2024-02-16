package com.github.kacperpotapczyk.pvoptimizer.backend.entity.constraint;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class TimeWindowConstraint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "constraint_value", nullable = false)
    private double constraintValue;

    @Column(name = "date_time_start", nullable = false)
    private LocalDateTime dateTimeStart;

    @Column(name = "date_time_end", nullable = false)
    private LocalDateTime dateTimeEnd;

    public TimeWindowConstraint(TimeWindowConstraint timeWindowConstraint) {
        this.id = timeWindowConstraint.getId();
        this.constraintValue = timeWindowConstraint.getConstraintValue();
        this.dateTimeStart = timeWindowConstraint.getDateTimeStart();
        this.dateTimeEnd = timeWindowConstraint.getDateTimeEnd();
    }

    public boolean isActiveInTimeWindow(LocalDateTime windowStart, LocalDateTime windowEnd) {

        if (dateTimeStart.isBefore(windowStart) && dateTimeEnd.isAfter(windowEnd)) {
            return true;
        }
        return isBetweenClosedRange(dateTimeStart, windowStart, windowEnd) || isBetweenClosedRange(dateTimeEnd, windowStart, windowEnd);
    }

    private boolean isBetweenClosedRange(LocalDateTime dateTime, LocalDateTime windowStart, LocalDateTime windowEnd) {

        return (windowStart.isBefore(dateTime) && dateTime.isBefore(windowEnd)) || dateTime.isEqual(windowStart) || dateTime.isEqual(windowEnd);
    }
}