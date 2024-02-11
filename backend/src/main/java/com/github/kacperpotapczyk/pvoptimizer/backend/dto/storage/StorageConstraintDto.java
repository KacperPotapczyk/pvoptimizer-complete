package com.github.kacperpotapczyk.pvoptimizer.backend.dto.storage;

import java.time.LocalDateTime;

public record StorageConstraintDto(
        LocalDateTime dateTimeStart,
        LocalDateTime dateTimeEnd,
        double constraintValue
) {
}
