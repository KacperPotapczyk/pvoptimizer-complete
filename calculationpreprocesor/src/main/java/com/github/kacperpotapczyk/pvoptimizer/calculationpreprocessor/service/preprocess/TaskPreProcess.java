package com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.service.preprocess;

import com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskCalculationDto;

public interface TaskPreProcess {

    PreProcessResult preProcess(TaskCalculationDto taskCalculationDto);
}
