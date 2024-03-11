package com.github.kacperpotapczyk.pvoptimizer.backend.dto.task;

import java.time.LocalDateTime;
import java.util.Set;

public record TaskDto(
        String name,
        LocalDateTime dateTimeStart,
        LocalDateTime dateTimeEnd,
        boolean readOnly,
        long timeOutInSeconds,
        double relativeGap,
        long intervalLengthMinutes,
        Set<TaskBaseObjectRevisionDto> demandRevisions,
        Set<TaskBaseObjectRevisionDto> productionRevisions,
        Set<TaskBaseObjectRevisionDto> tariffRevisions,
        Set<TaskBaseObjectRevisionDto> contractRevisions,
        Set<TaskBaseObjectRevisionDto> storageRevisions,
        Set<TaskBaseObjectRevisionDto> movableDemandRevisions
) {}
