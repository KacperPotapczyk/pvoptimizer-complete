package com.github.kacperpotapczyk.pvoptimizer.backend.dto.production;

import java.util.Set;

public record ProductionRevisionsListDto(String name, Set<ProductionRevisionDto> revisions) {
}
