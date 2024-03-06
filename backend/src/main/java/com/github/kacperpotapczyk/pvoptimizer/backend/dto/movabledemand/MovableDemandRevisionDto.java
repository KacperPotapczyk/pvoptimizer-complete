package com.github.kacperpotapczyk.pvoptimizer.backend.dto.movabledemand;

import java.util.List;
import java.util.Set;

public record MovableDemandRevisionDto(
        long revisionNumber,
        Set<MovableDemandStartDto> movableDemandStarts,
        List<MovableDemandValueDto> movableDemandValues
) {
}
