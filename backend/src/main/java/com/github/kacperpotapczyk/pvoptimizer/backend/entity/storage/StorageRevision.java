package com.github.kacperpotapczyk.pvoptimizer.backend.entity.storage;

import com.github.kacperpotapczyk.pvoptimizer.backend.entity.Revision;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.constraint.TimeWindowConstraint;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "storage_revision", uniqueConstraints = {@UniqueConstraint(name = "uc_storage_revision_number", columnNames = {"base_id", "revision_number"})})
public class StorageRevision extends Revision {

    @ManyToOne
    @JoinColumn(name = "base_id")
    private Storage storage;

    @Column(name = "initial_energy", nullable = false)
    private double initialEnergy;

    @OneToMany(mappedBy = "storageRevision", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StorageMinChargeConstraint> storageMinChargeConstraints;

    @OneToMany(mappedBy = "storageRevision", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StorageMaxChargeConstraint> storageMaxChargeConstraints;

    @OneToMany(mappedBy = "storageRevision", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StorageMinDischargeConstraint> storageMinDischargeConstraints;

    @OneToMany(mappedBy = "storageRevision", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StorageMaxDischargeConstraint> storageMaxDischargeConstraints;

    @OneToMany(mappedBy = "storageRevision", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StorageMinEnergyConstraint> storageMinEnergyConstraints;

    @OneToMany(mappedBy = "storageRevision", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StorageMaxEnergyConstraint> storageMaxEnergyConstraints;

    public List<TimeWindowConstraint> getMinChargeConstraintsInTimeWindow(LocalDateTime windowStart, LocalDateTime windowEnd) {

        return getConstraintsInTimeWindow(storageMinChargeConstraints, windowStart, windowEnd);
    }

    public List<TimeWindowConstraint> getMaxChargeConstraintsInTimeWindow(LocalDateTime windowStart, LocalDateTime windowEnd) {

        return getConstraintsInTimeWindow(storageMaxChargeConstraints, windowStart, windowEnd);
    }

    public List<TimeWindowConstraint> getMinDischargeConstraintsInTimeWindow(LocalDateTime windowStart, LocalDateTime windowEnd) {

        return getConstraintsInTimeWindow(storageMinDischargeConstraints, windowStart, windowEnd);
    }

    public List<TimeWindowConstraint> getMaxDischargeConstraintsInTimeWindow(LocalDateTime windowStart, LocalDateTime windowEnd) {

        return getConstraintsInTimeWindow(storageMaxDischargeConstraints, windowStart, windowEnd);
    }

    public List<TimeWindowConstraint> getMinEnergyConstraintsInTimeWindow(LocalDateTime windowStart, LocalDateTime windowEnd) {

        return getConstraintsInTimeWindow(storageMinEnergyConstraints, windowStart, windowEnd);
    }

    public List<TimeWindowConstraint> getMaxEnergyConstraintsInTimeWindow(LocalDateTime windowStart, LocalDateTime windowEnd) {

        return getConstraintsInTimeWindow(storageMaxEnergyConstraints, windowStart, windowEnd);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StorageRevision that)) return false;
        if (!super.equals(o)) return false;

        return getStorage() != null ? getStorage().equals(that.getStorage()) : that.getStorage() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getStorage() != null ? getStorage().hashCode() : 0);
        return result;
    }

    @Override
    public void softDelete() {
        this.setDeleted(true);
    }

    private List<TimeWindowConstraint> getConstraintsInTimeWindow(List<? extends TimeWindowConstraint> contractConstraints, LocalDateTime windowStart, LocalDateTime windowEnd) {

        return contractConstraints.stream()
                .filter(contractConstraint -> contractConstraint.isActiveInTimeWindow(windowStart, windowEnd))
                .map(TimeWindowConstraint::new)
                .toList();
    }
}