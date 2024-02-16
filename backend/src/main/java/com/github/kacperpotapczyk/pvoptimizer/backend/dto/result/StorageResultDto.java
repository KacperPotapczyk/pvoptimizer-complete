package com.github.kacperpotapczyk.pvoptimizer.backend.dto.result;

import java.util.List;

public record StorageResultDto(
        String storageName,
        long revisionNumber,
        List<StorageResultValueDto> storageResultValues
) {
}
