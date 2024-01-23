package com.github.kacperpotapczyk.pvoptimizer.backend.dto.contract;

import java.util.List;

public record ContractRevisionDto(
        long revisionNumber,
        List<ContractConstraintDto> contractMinPowerConstraints,
        List<ContractConstraintDto> contractMaxPowerConstraints,
        List<ContractConstraintDto> contractMinEnergyConstraints,
        List<ContractConstraintDto> contractMaxEnergyConstraints
) {}
