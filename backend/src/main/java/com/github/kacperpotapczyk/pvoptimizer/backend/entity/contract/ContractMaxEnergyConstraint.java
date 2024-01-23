package com.github.kacperpotapczyk.pvoptimizer.backend.entity.contract;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "contract_max_energy_constraint", indexes = {@Index(name = "idx_contract_max_energy_constraint_revision_id", columnList = "revision_id")})
public class ContractMaxEnergyConstraint extends ContractConstraint {

    @ManyToOne
    @JoinColumn(name = "revision_id")
    private ContractRevision contractRevision;
}
