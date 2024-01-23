package com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.service.validation;

import com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskCalculationDto;
import com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.message.ValidationMessages;

public interface ValidationService {

    ValidationMessages validate(TaskCalculationDto taskCalculationDto);
}
