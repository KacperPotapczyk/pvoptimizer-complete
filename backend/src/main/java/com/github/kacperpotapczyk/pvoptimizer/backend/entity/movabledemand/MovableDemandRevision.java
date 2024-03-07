package com.github.kacperpotapczyk.pvoptimizer.backend.entity.movabledemand;

import com.github.kacperpotapczyk.pvoptimizer.backend.entity.Revision;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(
        name = "movable_demand_revision",
        uniqueConstraints = {@UniqueConstraint(name = "uc_movable_demand_revision_number", columnNames = {"base_id", "revision_number"})}
)
public class MovableDemandRevision extends Revision {

    @ManyToOne
    @JoinColumn(name = "base_id")
    private MovableDemand movableDemand;

    @OneToMany(mappedBy = "movableDemandRevision", cascade = CascadeType.ALL)
    private Set<MovableDemandStart> movableDemandStarts = new HashSet<>();

    @OneToMany(mappedBy = "movableDemandRevision", cascade = CascadeType.ALL)
    private List<MovableDemandValue> movableDemandValues = new ArrayList<>();

    public MovableDemandRevision(long revisionNumber, Set<MovableDemandStart> movableDemandStarts, List<MovableDemandValue> movableDemandValues) {
        super(revisionNumber);
        this.movableDemandStarts = movableDemandStarts;
        this.movableDemandValues = movableDemandValues;
    }

    public List<MovableDemandStart> getMovableDemandStartsForTimeWindow(LocalDateTime windowStart, LocalDateTime windowEnd) {

        long profileLengthMinutes = this.movableDemandValues.stream()
                .map(MovableDemandValue::getDurationMinutes)
                .reduce(0L, Long::sum);

        return this.movableDemandStarts.stream()
                .filter(movableDemandStart -> isActiveInTimeWindow(
                        movableDemandStart.getStart(),
                        movableDemandStart.getStart().plusMinutes(profileLengthMinutes),
                        windowStart,
                        windowEnd))
                .toList();
    }

    private boolean isActiveInTimeWindow(LocalDateTime rangeStart, LocalDateTime rangeEnd, LocalDateTime windowStart, LocalDateTime windowEnd) {

        if (rangeStart.isBefore(windowStart) && rangeEnd.isAfter(windowEnd)) {
            return true;
        }
        return isBetweenClosedRange(rangeStart, windowStart, windowEnd) || isBetweenClosedRange(rangeEnd, windowStart, windowEnd);
    }

    private boolean isBetweenClosedRange(LocalDateTime dateTime, LocalDateTime windowStart, LocalDateTime windowEnd) {

        return (windowStart.isBefore(dateTime) && dateTime.isBefore(windowEnd)) || dateTime.isEqual(windowStart) || dateTime.isEqual(windowEnd);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MovableDemandRevision that)) return false;
        if (!super.equals(o)) return false;

        return getMovableDemand() != null ? getMovableDemand().equals(that.getMovableDemand()) : that.getMovableDemand() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getMovableDemand() != null ? getMovableDemand().hashCode() : 0);
        return result;
    }

    @Override
    public void softDelete() {
        setDeleted(true);
    }
}