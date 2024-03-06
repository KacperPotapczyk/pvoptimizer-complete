package com.github.kacperpotapczyk.pvoptimizer.backend.dto.movabledemand;

import java.util.Set;

public record MovableDemandRevisionsListDto(String name, Set<MovableDemandRevisionListDto> revisions) {
}
