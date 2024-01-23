package com.github.kacperpotapczyk.pvoptimizer.backend.service;

import com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.result.TaskCalculationResultDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.result.TaskResult;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.result.ValidationMessage;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.task.Task;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TaskResultService {

    TaskResult getResultForTaskId(long taskId);
    TaskResult getResultForTaskName(String taskName);
    TaskResult initiateResult(Task task);
    TaskResult addValidationResult(long taskId, List<ValidationMessage> validationMessages);
    TaskResult addCalculationResult(TaskCalculationResultDto taskCalculationResult);
}
