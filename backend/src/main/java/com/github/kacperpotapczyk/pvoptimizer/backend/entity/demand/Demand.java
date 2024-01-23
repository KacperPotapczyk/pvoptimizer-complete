package com.github.kacperpotapczyk.pvoptimizer.backend.entity.demand;

import com.github.kacperpotapczyk.pvoptimizer.backend.entity.BaseObject;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "demand", uniqueConstraints = {@UniqueConstraint(name = "uc_demand_name_is_deleted", columnNames = {"name", "is_deleted"})})
public class Demand extends BaseObject<DemandRevision> {

    @OneToMany(mappedBy = "demand", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DemandRevision> revisions;

    public Set<DemandRevision> getRevisions() {
        return this.revisions.stream()
                .filter(demandRevision -> !demandRevision.isDeleted())
                .collect(Collectors.toSet());
    }

    public void addRevision(DemandRevision demandRevision) {

        if (this.revisions == null) {
            this.revisions = new HashSet<>();
        }

        this.revisions.add(demandRevision);
        demandRevision.setDemand(this);
    }

    public Optional<DemandRevision> getRevision(long revisionNumber) {

        return revisions.stream()
                .filter(revision -> revision.getRevisionNumber() == revisionNumber)
                .findFirst();
    }

    public Optional<Long> getLastRevisionNumber() {

        return revisions.stream()
                .map(DemandRevision::getRevisionNumber)
                .max(Long::compareTo);
    }

    @Override
    public void softDelete() {
        setDeleted(true);
        this.revisions.forEach(DemandRevision::softDelete);
    }
}
