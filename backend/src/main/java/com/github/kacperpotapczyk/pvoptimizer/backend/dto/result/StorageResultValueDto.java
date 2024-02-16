package com.github.kacperpotapczyk.pvoptimizer.backend.dto.result;

import java.time.LocalDateTime;

public record StorageResultValueDto(
        LocalDateTime dateTimeStart,
        LocalDateTime dateTimeEnd,
        double charge,
        double discharge,
        double energy,
        StorageModeDto storageMode
) {
}
