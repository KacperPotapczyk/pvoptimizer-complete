package com.github.kacperpotapczyk.pvoptimizer.backend.dto.tariff;

import java.util.Set;

public record TariffRevisionsListDto(String name, Set<TariffRevisionListDto> revisions) {
}
