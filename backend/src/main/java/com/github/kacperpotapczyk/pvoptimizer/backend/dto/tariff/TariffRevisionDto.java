package com.github.kacperpotapczyk.pvoptimizer.backend.dto.tariff;

import com.github.kacperpotapczyk.pvoptimizer.backend.dto.cyclicalvalue.CyclicalDailyValueDto;

import java.util.List;

public record TariffRevisionDto(
        long revisionNumber,
        double defaultPrice,
        List<CyclicalDailyValueDto> cyclicalDailyValues
) {
}
