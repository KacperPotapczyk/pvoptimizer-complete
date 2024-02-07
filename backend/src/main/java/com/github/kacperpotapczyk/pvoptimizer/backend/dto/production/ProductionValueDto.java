package com.github.kacperpotapczyk.pvoptimizer.backend.dto.production;

import java.time.LocalDateTime;

public record ProductionValueDto(LocalDateTime dateTime, double value) {
}
