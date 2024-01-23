package com.github.kacperpotapczyk.pvoptimizer.backend.dto.demand;

import java.util.Set;

public record DemandRevisionsListDto(String name, Set<DemandRevisionListDto> revisions) {}
