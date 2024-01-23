package com.github.kacperpotapczyk.pvoptimizer.backend.entity.demand;

import com.github.kacperpotapczyk.pvoptimizer.backend.entity.Revision;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "demand_revision",
        uniqueConstraints = {@UniqueConstraint(name = "uc_demand_revision_number", columnNames = {"base_id", "revision_number"})}
)
public class DemandRevision extends Revision {

    @ManyToOne
    @JoinColumn(name = "base_id")
    private Demand demand;

    @OneToMany(mappedBy = "demandRevision", cascade = CascadeType.ALL)
    private Set<DemandValue> demandValues = new HashSet<>();

    public DemandRevision(long revisionNumber, Set<DemandValue> demandValues) {
        super(revisionNumber);
        this.demandValues = demandValues;
    }

    public Set<DemandValue> getDemandValuesInTimeWindow(LocalDateTime windowStart, LocalDateTime windowEnd) {

        return demandValues.stream()
                .filter(demandValue -> demandValue.isActiveInTimeWindow(windowStart, windowEnd))
                .collect(Collectors.toSet());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DemandRevision that)) return false;
        if (!super.equals(o)) return false;

        return getDemand() != null ? getDemand().equals(that.getDemand()) : that.getDemand() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getDemand() != null ? getDemand().hashCode() : 0);
        return result;
    }

    @Override
    public void softDelete() {
        setDeleted(true);
    }
}
