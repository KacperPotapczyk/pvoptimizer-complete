package com.github.kacperpotapczyk.pvoptimizer.backend.dto.result;

import java.time.LocalDateTime;

public record MovableDemandResultValueDto(
        LocalDateTime dateTimeStart,
        LocalDateTime dateTimeEnd,
        double power,
        double energy
) {
}
