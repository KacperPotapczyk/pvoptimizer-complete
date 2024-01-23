package com.github.kacperpotapczyk.pvoptimizer.backend.dto.result;

public enum ResultStatusDto {
    STARTED,
    VALIDATION_SUCCESSFUL,
    VALIDATION_WARNING,
    VALIDATION_ERROR,
    OPTIMIZER_ERROR,
    SOLUTION_FOUND,
    SOLUTION_NOT_FOUND
}
