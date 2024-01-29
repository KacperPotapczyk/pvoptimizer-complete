package com.github.kacperpotapczyk.pvoptimizer.backend.dto.production;

import java.util.Set;

public record ProductionRevisionDto(long revisionNumber, Set<ProductionValueDto> productionValues) {
}
