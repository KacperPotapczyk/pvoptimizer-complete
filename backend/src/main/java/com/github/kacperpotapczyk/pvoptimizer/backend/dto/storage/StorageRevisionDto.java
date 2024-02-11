package com.github.kacperpotapczyk.pvoptimizer.backend.dto.storage;

import java.util.List;

public record StorageRevisionDto(
        long revisionNumber,
        double initialEnergy,
        List<StorageConstraintDto> storageMinChargeConstraints,
        List<StorageConstraintDto> storageMaxChargeConstraints,
        List<StorageConstraintDto> storageMinDischargeConstraints,
        List<StorageConstraintDto> storageMaxDischargeConstraints,
        List<StorageConstraintDto> storageMinEnergyConstraints,
        List<StorageConstraintDto> storageMaxEnergyConstraints
) {
}
