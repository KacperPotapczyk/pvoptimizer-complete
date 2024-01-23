package com.github.kacperpotapczyk.pvoptimizer.backend.dto.result;

import java.time.LocalDateTime;

public record ContractResultValueDto(
        LocalDateTime dateTimeStart,
        LocalDateTime dateTimeEnd,
        double power,
        double energy,
        double cost
) {
}
