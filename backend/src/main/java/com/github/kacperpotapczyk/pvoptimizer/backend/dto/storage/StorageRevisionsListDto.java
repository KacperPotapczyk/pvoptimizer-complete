package com.github.kacperpotapczyk.pvoptimizer.backend.dto.storage;

import java.util.Set;

public record StorageRevisionsListDto(
        String name,
        Set<StorageRevisionListDto> revisions
) {
}
