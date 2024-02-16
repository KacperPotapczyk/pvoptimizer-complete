package com.github.kacperpotapczyk.pvoptimizer.backend.dto.storage;

import java.util.Set;

public record StorageDto(
        String name,
        double capacity,
        double maxCharge,
        double maxDischarge,
        Set<StorageRevisionDto> revisions
) {
}
