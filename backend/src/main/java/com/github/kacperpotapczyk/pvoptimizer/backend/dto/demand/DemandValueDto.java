package com.github.kacperpotapczyk.pvoptimizer.backend.dto.demand;

import java.time.LocalDateTime;

public record DemandValueDto(LocalDateTime dateTime, double value) {}
