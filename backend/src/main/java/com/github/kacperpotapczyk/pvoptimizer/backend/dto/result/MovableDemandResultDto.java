package com.github.kacperpotapczyk.pvoptimizer.backend.dto.result;

import java.util.List;

public record MovableDemandResultDto(
        String movableDemandName,
        long revisionNumber,
        List<MovableDemandResultValueDto> movableDemandResultValues
) {
}
