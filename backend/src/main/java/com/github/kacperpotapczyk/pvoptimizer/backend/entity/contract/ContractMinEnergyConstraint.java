package com.github.kacperpotapczyk.pvoptimizer.backend.entity.contract;

import com.github.kacperpotapczyk.pvoptimizer.backend.entity.constraint.TimeWindowConstraint;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "contract_min_energy_constraint", indexes = {@Index(name = "idx_contract_min_energy_constraint_revision_id", columnList = "revision_id")})
public class ContractMinEnergyConstraint extends TimeWindowConstraint {

    @ManyToOne
    @JoinColumn(name = "revision_id")
    private ContractRevision contractRevision;
}
