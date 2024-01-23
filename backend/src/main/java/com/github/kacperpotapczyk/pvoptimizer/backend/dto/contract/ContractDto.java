package com.github.kacperpotapczyk.pvoptimizer.backend.dto.contract;

import java.util.Set;

public record ContractDto(
        String name,
        ContractTypeDto contractType,
        ContractTariffDto tariff,
        Set<ContractRevisionDto> revisions
) {}
