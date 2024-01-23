package com.github.kacperpotapczyk.pvoptimizer.backend.dto.result;

import java.util.List;

public record ContractResultDto(
        String contractName,
        long revisionNumber,
        List<ContractResultValueDto> contractResultValues
) {
}
