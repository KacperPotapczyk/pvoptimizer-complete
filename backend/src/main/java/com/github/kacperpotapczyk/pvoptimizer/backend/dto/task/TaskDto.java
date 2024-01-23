package com.github.kacperpotapczyk.pvoptimizer.backend.dto.task;

import java.time.LocalDateTime;
import java.util.Set;

public record TaskDto(
        String name,
        LocalDateTime dateTimeStart,
        LocalDateTime dateTimeEnd,
        Set<TaskBaseObjectRevisionDto> demandRevisions,
        Set<TaskBaseObjectRevisionDto> tariffRevisions,
        Set<TaskBaseObjectRevisionDto> contractRevisions
) {}
