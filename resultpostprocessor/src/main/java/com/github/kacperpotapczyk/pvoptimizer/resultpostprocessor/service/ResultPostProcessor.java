package com.github.kacperpotapczyk.pvoptimizer.resultpostprocessor.service;

import com.github.kacperpotapczyk.pvoptimizer.avro.postprocessor.TaskPostProcessDataDto;
import com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.result.TaskCalculationResultDto;
import com.github.kacperpotapczyk.pvoptimizer.avro.optimizer.result.ResultDto;

public interface ResultPostProcessor {

    TaskCalculationResultDto postProcess(TaskPostProcessDataDto taskPostProcessDataDto, ResultDto resultDto);
}
