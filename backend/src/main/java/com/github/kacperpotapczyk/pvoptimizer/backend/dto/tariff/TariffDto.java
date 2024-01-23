package com.github.kacperpotapczyk.pvoptimizer.backend.dto.tariff;

import java.util.Set;

public record TariffDto(String name, Set<TariffRevisionDto> revisions) {}
