package com.github.kacperpotapczyk.pvoptimizer.backend.dto.storage;

import java.util.Date;

public record StorageRevisionListDto(
        long revisionNumber,
        Date createdDate
) {
}
