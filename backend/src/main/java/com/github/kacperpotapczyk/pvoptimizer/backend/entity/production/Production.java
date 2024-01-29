package com.github.kacperpotapczyk.pvoptimizer.backend.entity.production;

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
@Table(
        name = "production",
        uniqueConstraints = {@UniqueConstraint(name = "uc_production_name_is_deleted", columnNames = {"name", "is_deleted"})}
)
public class Production extends BaseObject<ProductionRevision> {

    @OneToMany(mappedBy = "production", cascade = CascadeType.ALL)
    private Set<ProductionRevision> revisions;

    @Override
    public Set<ProductionRevision> getRevisions() {
        return this.revisions.stream()
                .filter(productionRevision -> !productionRevision.isDeleted())
                .collect(Collectors.toSet());
    }

    @Override
    public void addRevision(ProductionRevision productionRevision) {

        if (this.revisions == null) {
            this.revisions = new HashSet<>();
        }

        this.revisions.add(productionRevision);
        productionRevision.setProduction(this);
    }

    @Override
    public Optional<ProductionRevision> getRevision(long revisionNumber) {
        return revisions.stream()
                .filter(revision -> revision.getRevisionNumber() == revisionNumber)
                .findFirst();
    }

    @Override
    public Optional<Long> getLastRevisionNumber() {
        return revisions.stream()
                .map(ProductionRevision::getRevisionNumber)
                .max(Long::compareTo);
    }

    @Override
    public void softDelete() {
        setDeleted(true);
        this.revisions.forEach(ProductionRevision::softDelete);
    }
}