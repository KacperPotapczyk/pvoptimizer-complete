package com.github.kacperpotapczyk.pvoptimizer.backend.entity.storage;

import com.github.kacperpotapczyk.pvoptimizer.backend.entity.constraint.TimeWindowConstraint;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "storage_min_charge_constraint", indexes = {@Index(name = "idx_storage_min_charge_constraint_revision_id", columnList = "revision_id")})
public class StorageMinChargeConstraint extends TimeWindowConstraint {

    @ManyToOne
    @JoinColumn(name = "revision_id")
    private StorageRevision storageRevision;
}