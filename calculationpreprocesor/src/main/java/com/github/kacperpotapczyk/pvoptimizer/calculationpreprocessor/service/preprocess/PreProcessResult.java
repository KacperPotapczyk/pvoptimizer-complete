package com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.service.preprocess;

import com.github.kacperpotapczyk.pvoptimizer.avro.postprocessor.TaskPostProcessDataDto;
import com.github.kacperpotapczyk.pvoptimizer.avro.optimizer.task.TaskDto;

public record PreProcessResult(
        TaskDto taskDto,
        TaskPostProcessDataDto taskPostProcessDataDto
) {
}
