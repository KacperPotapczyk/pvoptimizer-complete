package com.github.kacperpotapczyk.pvoptimizer.backend.dto.movabledemand;

public record MovableDemandValueDto(
        long order,
        long durationMinutes,
        double value
) {
}
