package com.github.kacperpotapczyk.pvoptimizer.backend.dto.result;

public record ValidationMessageDto(
        ValidationMessageLevelDto level,
        ValidationMessageObjectTypeDto objectType,
        String objectName,
        long objectRevision,
        String message
) {}
