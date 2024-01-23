package com.github.kacperpotapczyk.pvoptimizer.backend.dto.contract;

import java.util.Set;

public record ContractRevisionsListDto(
        String name,
        Set<ContractRevisionListDto> revisions
) {
}
