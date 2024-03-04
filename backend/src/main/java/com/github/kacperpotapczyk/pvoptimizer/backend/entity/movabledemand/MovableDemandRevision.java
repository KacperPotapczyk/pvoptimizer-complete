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

    public List<MovableDemandValue> getMovableDemandValuesForTimeWindowAndStartDateTime(LocalDateTime startDateTime ,LocalDateTime windowStart, LocalDateTime windowEnd) {

        List<MovableDemandValue> movableDemandValueList = new ArrayList<>();

        Optional<MovableDemandValue> movableDemandValueBeforeWindowStart = movableDemandValues.stream()
                .filter(movableDemandValue -> startDateTime.plusMinutes(movableDemandValue.getDurationMinutes()).isAfter(windowStart))
                .max(Comparator.comparing(movableDemandValue -> startDateTime.plusMinutes(movableDemandValue.getDurationMinutes())));
        movableDemandValueBeforeWindowStart.ifPresent(movableDemandValueList::add);

        movableDemandValueList.addAll(movableDemandValues.stream()
                .filter(movableDemandValue -> movableDemandValue.isActiveInTimeWindow(startDateTime, windowStart, windowEnd))
                .sorted()
                .toList()
        );

        return movableDemandValueList;
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