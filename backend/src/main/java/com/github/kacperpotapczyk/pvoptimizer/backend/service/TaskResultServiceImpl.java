package com.github.kacperpotapczyk.pvoptimizer.backend.service;

import com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.result.TaskCalculationContractResultDto;
import com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.result.TaskCalculationContractResultValueDto;
import com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.result.TaskCalculationResultDto;
import com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.result.TaskCalculationResultStatusDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.contract.ContractRevision;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.result.*;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.task.Task;
import com.github.kacperpotapczyk.pvoptimizer.backend.mapper.DateMapper;
import com.github.kacperpotapczyk.pvoptimizer.backend.mapper.TaskCalculationResultMapper;
import com.github.kacperpotapczyk.pvoptimizer.backend.repository.TaskRepository;
import com.github.kacperpotapczyk.pvoptimizer.backend.repository.TaskResultRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class TaskResultServiceImpl implements TaskResultService {

    private final TaskResultRepository resultRepository;
    private final TaskRepository taskRepository;
    private final TaskCalculationResultMapper taskCalculationResultMapper;
    private final DateMapper dateMapper;

    @Autowired
    public TaskResultServiceImpl(TaskResultRepository resultRepository, TaskRepository taskRepository, TaskCalculationResultMapper taskCalculationResultMapper, DateMapper dateMapper) {
        this.resultRepository = resultRepository;
        this.taskRepository = taskRepository;
        this.taskCalculationResultMapper = taskCalculationResultMapper;
        this.dateMapper = dateMapper;
    }

    @Override
    public TaskResult getResultForTaskId(long taskId) {

        return resultRepository.findByTaskId(taskId)
                .orElseThrow(() -> new ObjectNotFoundException(TaskResult.class, "For task id: " + taskId));
    }

    @Override
    public TaskResult getResultForTaskName(String taskName) {

        return resultRepository.findByTaskName(taskName)
                .orElseThrow(() -> new ObjectNotFoundException(TaskResult.class, "For task name" + taskName));
    }

    @Override
    public TaskResult initiateResult(Task task) {

        if (!resultRepository.existsByTaskName(task.getName())) {

            TaskResult taskResult = new TaskResult(task);
            return resultRepository.save(taskResult);
        }
        else {
            throw new IllegalArgumentException("Results for task with given name: " + task.getName() + " already exists in DB");
        }
    }

    @Override
    @Transactional
    public TaskResult addValidationResult(long taskId, List<ValidationMessage> validationMessages) {

        log.debug("Adding validation result for task with id: {}", taskId);
        TaskResult taskResult = getResultForTaskId(taskId);
        validationMessages.forEach(validationMessage -> validationMessage.setTaskResult(taskResult));

        taskResult.setValidationMessages(validationMessages);

        if (validationMessages.stream().anyMatch(validationMessage -> validationMessage.getLevel() == ValidationMessageLevel.ERROR)) {
            taskResult.setResultStatus(ResultStatus.VALIDATION_ERROR);
            log.error("Task with id: {} validated with an error", taskId);
        } else if (validationMessages.stream().anyMatch(validationMessage -> validationMessage.getLevel() == ValidationMessageLevel.WARNING)) {
            taskResult.setResultStatus(ResultStatus.VALIDATION_WARNING);
            log.warn("Task with id: {} validated successfully with a warning", taskId);
        } else {
            taskResult.setResultStatus(ResultStatus.VALIDATION_SUCCESSFUL);
            log.debug("Task with id: {} validated successfully", taskId);
        }

        return resultRepository.save(taskResult);
    }

    @Override
    @Transactional
    public TaskResult addCalculationResult(TaskCalculationResultDto taskCalculationResult) {

        long taskId = taskCalculationResult.getId();
        log.debug("Adding calculation result for task with id: {}", taskId);
        TaskResult taskResult = getResultForTaskId(taskId);

        if (taskResult.getResultStatus() == ResultStatus.VALIDATION_SUCCESSFUL || taskResult.getResultStatus() == ResultStatus.VALIDATION_WARNING) {

            taskResult.setResultStatus(taskCalculationResultMapper.mapTaskCalculationResultStatusDtoToResultStatus(taskCalculationResult.getResultStatus()));
            taskResult.setOptimizerMessage(taskCalculationResult.getOptimizerMessage().toString());

            if (taskCalculationResult.getResultStatus() == TaskCalculationResultStatusDto.SOLUTION_FOUND) {

                log.debug("Task with id: {} optimized successfully", taskId);

                taskResult.setObjectiveFunctionValue(taskCalculationResult.getObjectiveFunctionValue());
                taskResult.setRelativeGap(taskCalculationResult.getRelativeGap());
                taskResult.setElapsedTime(taskCalculationResult.getElapsedTime());

                for (TaskCalculationContractResultDto contractResultDto : taskCalculationResult.getContractResults()) {
                    taskResult.addContractResult(mapTaskCalculationContractResultDtoToContractResult(taskCalculationResult, contractResultDto, taskResult));
                }
            } else {
                log.error("Task with id: {} optimization failed with message: {}", taskId, taskCalculationResult.getOptimizerMessage().toString());
            }
            return resultRepository.save(taskResult);
        } else {
            log.error("Could not add calculation results for task with id: {} when task validation is not successful", taskId);
            throw new IllegalStateException("Could not add calculation results when task validation is not successful");
        }
    }

    private ContractResult mapTaskCalculationContractResultDtoToContractResult(TaskCalculationResultDto taskCalculationResult, TaskCalculationContractResultDto taskCalculationContractResultDto, TaskResult taskResult) {

        ContractRevision contractRevision = taskRepository.getContractRevisionByTaskIdAndContractId(
                taskCalculationResult.getId(),
                taskCalculationContractResultDto.getId()
        );

        ContractResult contractResult = new ContractResult();
        contractResult.setContractRevision(contractRevision);
        contractResult.setTaskResult(taskResult);

        List<ContractResultValue> contractResultValues = new ArrayList<>();

        List<TaskCalculationContractResultValueDto> contractResultValueDtoListSorted = taskCalculationContractResultDto.getContractResultValues().stream()
                .sorted(Comparator.comparing(v -> dateMapper.asLocalDateTime(v.getDateTimeStart())))
                .toList();

        for (TaskCalculationContractResultValueDto contractResultValueDto : contractResultValueDtoListSorted) {

            ContractResultValue contractResultValue = taskCalculationResultMapper.mapTaskCalculationContractResultValueDtoToContractResultValue(contractResultValueDto);
            contractResultValue.setContractResult(contractResult);
            contractResultValues.add(contractResultValue);
        }

        contractResult.setContractResultValues(contractResultValues);
        return contractResult;
    }
}
