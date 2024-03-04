package com.github.kacperpotapczyk.pvoptimizer.backend.entity.movabledemand;

import com.github.kacperpotapczyk.pvoptimizer.backend.entity.BaseObject;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Table(name = "movable_demand", uniqueConstraints = {@UniqueConstraint(name = "uc_movable_demand_name_is_deleted", columnNames = {"name", "is_deleted"})})
public class MovableDemand extends BaseObject<MovableDemandRevision> {

    @OneToMany(mappedBy = "movableDemand", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MovableDemandRevision> revisions;

    @Override
    public Set<MovableDemandRevision> getRevisions() {
        return this.revisions.stream()
                .filter(movableDemandRevision -> !movableDemandRevision.isDeleted())
                .collect(Collectors.toSet());
    }

    @Override
    public void addRevision(MovableDemandRevision movableDemandRevision) {

        if (this.revisions == null) {
            this.revisions = new HashSet<>();
        }

        this.revisions.add(movableDemandRevision);
        movableDemandRevision.setMovableDemand(this);
    }

    @Override
    public Optional<MovableDemandRevision> getRevision(long revisionNumber) {

        return revisions.stream()
                .filter(revision -> revision.getRevisionNumber() == revisionNumber)
                .findFirst();
    }

    @Override
    public Optional<Long> getLastRevisionNumber() {

        return revisions.stream()
                .map(MovableDemandRevision::getRevisionNumber)
                .max(Long::compareTo);
    }

    @Override
    public void softDelete() {
        setDeleted(true);
        this.revisions.forEach(MovableDemandRevision::softDelete);
    }
}