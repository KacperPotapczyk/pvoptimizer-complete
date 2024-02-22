package com.github.kacperpotapczyk.pvoptimizer.backend.dto.cyclicalvalue;

import java.util.List;

public record CyclicalDailyValueDto(
        WeekdaysDto dayOfTheWeek,
        List<DailyTimeValueDto> dailyTimeValues
) {
}
