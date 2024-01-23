package com.github.kacperpotapczyk.pvoptimizer.backend.entity.result;

public enum ResultStatus {
    STARTED,
    VALIDATION_SUCCESSFUL,
    VALIDATION_WARNING,
    VALIDATION_ERROR,
    OPTIMIZER_ERROR,
    SOLUTION_FOUND,
    SOLUTION_NOT_FOUND
}
