package com.github.kacperpotapczyk.pvoptimizer.backend.dto.result;

import java.util.List;

public record TaskResultDto(
        String taskName,
        ResultStatusDto resultStatus,
        List<ValidationMessageDto> validationMessages,
        Double objectiveFunctionValue,
        Double relativeGap,
        Double elapsedTime,
        String optimizerMessage,
        List<ContractResultDto> contractResults,
        List<StorageResultDto> storageResults,
        List<MovableDemandResultDto> movableDemandResults
) {}
