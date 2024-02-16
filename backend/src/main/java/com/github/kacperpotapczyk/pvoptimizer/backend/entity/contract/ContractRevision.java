package com.github.kacperpotapczyk.pvoptimizer.backend.entity.contract;

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
@Table(name = "contract_revision", uniqueConstraints = {@UniqueConstraint(name = "uc_contract_revision_number", columnNames = {"base_id", "revision_number"})})
public class ContractRevision extends Revision {

    @ManyToOne
    @JoinColumn(name = "base_id")
    private Contract contract;

    @OneToMany(mappedBy = "contractRevision", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContractMinPowerConstraint> contractMinPowerConstraints;

    @OneToMany(mappedBy = "contractRevision", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContractMaxPowerConstraint> contractMaxPowerConstraints;

    @OneToMany(mappedBy = "contractRevision", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContractMinEnergyConstraint> contractMinEnergyConstraints;

    @OneToMany(mappedBy = "contractRevision", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContractMaxEnergyConstraint> contractMaxEnergyConstraints;

    public ContractRevision(long revisionNumber) {
        super(revisionNumber);
    }

    public List<TimeWindowConstraint> getMinPowerConstraintsInTimeWindow(LocalDateTime windowStart, LocalDateTime windowEnd) {

        return getConstraintsInTimeWindow(contractMinPowerConstraints, windowStart, windowEnd);
    }

    public List<TimeWindowConstraint> getMaxPowerConstraintsInTimeWindow(LocalDateTime windowStart, LocalDateTime windowEnd) {

        return getConstraintsInTimeWindow(contractMaxPowerConstraints, windowStart, windowEnd);
    }

    public List<TimeWindowConstraint> getMinEnergyConstraintsInTimeWindow(LocalDateTime windowStart, LocalDateTime windowEnd) {

        return getConstraintsInTimeWindow(contractMinEnergyConstraints, windowStart, windowEnd);
    }

    public List<TimeWindowConstraint> getMaxEnergyConstraintsInTimeWindow(LocalDateTime windowStart, LocalDateTime windowEnd) {

        return getConstraintsInTimeWindow(contractMaxEnergyConstraints, windowStart, windowEnd);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ContractRevision that)) return false;
        if (!super.equals(o)) return false;

        return getContract() != null ? getContract().equals(that.getContract()) : that.getContract() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getContract() != null ? getContract().hashCode() : 0);
        return result;
    }

    @Override
    public void softDelete() {
        setDeleted(true);
    }

    private List<TimeWindowConstraint> getConstraintsInTimeWindow(List<? extends TimeWindowConstraint> contractConstraints, LocalDateTime windowStart, LocalDateTime windowEnd) {

        return contractConstraints.stream()
                .filter(contractConstraint -> contractConstraint.isActiveInTimeWindow(windowStart, windowEnd))
                .map(TimeWindowConstraint::new)
                .toList();
    }
}