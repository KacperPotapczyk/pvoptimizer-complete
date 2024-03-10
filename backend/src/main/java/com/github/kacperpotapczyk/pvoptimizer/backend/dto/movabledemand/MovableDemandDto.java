package com.github.kacperpotapczyk.pvoptimizer.backend.dto.movabledemand;

import java.util.Set;

public record MovableDemandDto(
        String name,
        Set<MovableDemandRevisionDto> revisions
) {
}
