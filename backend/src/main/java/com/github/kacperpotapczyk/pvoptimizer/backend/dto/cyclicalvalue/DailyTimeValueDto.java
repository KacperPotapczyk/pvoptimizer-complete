package com.github.kacperpotapczyk.pvoptimizer.backend.dto.cyclicalvalue;

import java.time.LocalTime;

public record DailyTimeValueDto(
        LocalTime startTime,
        double currentValue
) {
}
