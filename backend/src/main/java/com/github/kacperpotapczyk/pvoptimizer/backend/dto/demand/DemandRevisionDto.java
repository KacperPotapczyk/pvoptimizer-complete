package com.github.kacperpotapczyk.pvoptimizer.backend.dto.demand;

import java.util.Set;

public record DemandRevisionDto(long revisionNumber, Set<DemandValueDto> demandValues) {}
