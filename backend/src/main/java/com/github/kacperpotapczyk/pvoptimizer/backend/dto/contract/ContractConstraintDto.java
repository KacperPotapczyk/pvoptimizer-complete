package com.github.kacperpotapczyk.pvoptimizer.backend.dto.contract;

import java.time.LocalDateTime;

public record ContractConstraintDto(
        LocalDateTime dateTimeStart,
        LocalDateTime dateTimeEnd,
        double constraintValue
) {}
