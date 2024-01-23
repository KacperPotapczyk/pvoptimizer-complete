package com.github.kacperpotapczyk.pvoptimizer.backend.dto.demand;

import java.util.Set;

public record DemandDto(String name, Set<DemandRevisionDto> revisions) {}
