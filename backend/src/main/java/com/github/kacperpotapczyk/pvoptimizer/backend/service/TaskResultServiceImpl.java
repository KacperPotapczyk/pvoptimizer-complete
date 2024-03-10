package com.github.kacperpotapczyk.pvoptimizer.backend.service;

import com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.result.*;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.contract.ContractRevision;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.movabledemand.MovableDemandRevision;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.result.*;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.storage.StorageRevision;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.task.Task;
import com.github.kacperpotapczyk.pvoptimizer.backend.mapper.DateTimeMapper;
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
    private final DateTimeMapper dateTimeMapper;

    @Autowired
    public TaskResultServiceImpl(TaskResultRepository resultRepository, TaskRepository taskRepository, TaskCalculationResultMapper taskCalculationResultMapper, DateTimeMapper dateTimeMapper) {
        this.resultRepository = resultRepository;
        this.taskRepository = taskRepository;
        this.taskCalculationResultMapper = taskCalculationResultMapper;
        this.dateTimeMapper = dateTimeMapper;
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

                for (TaskCalculationStorageResultDto storageResultDto : taskCalculationResult.getStorageResults()) {
                    taskResult.addStorageResult(mapTaskCalculationStorageResultDtoToStorageResult(taskCalculationResult, storageResultDto, taskResult));
                }

                for (TaskCalculationMovableDemandResultDto movableDemandResultDto : taskCalculationResult.getMovableDemandResults()) {
                    taskResult.addMovableDemandResult(mapTaskCalculationMovableDemandResultDtoToMovableDemandResult(taskCalculationResult, movableDemandResultDto, taskResult));
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
                .sorted(Comparator.comparing(v -> dateTimeMapper.dateTimeAsLocalDateTime(v.getDateTimeStart())))
                .toList();

        for (TaskCalculationContractResultValueDto contractResultValueDto : contractResultValueDtoListSorted) {

            ContractResultValue contractResultValue = taskCalculationResultMapper.mapTaskCalculationContractResultValueDtoToContractResultValue(contractResultValueDto);
            contractResultValue.setContractResult(contractResult);
            contractResultValues.add(contractResultValue);
        }

        contractResult.setContractResultValues(contractResultValues);
        return contractResult;
    }

    private StorageResult mapTaskCalculationStorageResultDtoToStorageResult(TaskCalculationResultDto taskCalculationResult, TaskCalculationStorageResultDto taskCalculationStorageResultDto, TaskResult taskResult) {

        StorageRevision storageRevision = taskRepository.getStorageRevisionByTaskIdAndStorageId(
                taskCalculationResult.getId(),
                taskCalculationStorageResultDto.getId()
        );

        StorageResult storageResult = new StorageResult();
        storageResult.setStorageRevision(storageRevision);
        storageResult.setTaskResult(taskResult);

        List<StorageResultValue> storageResultValues = new ArrayList<>();

        List<TaskCalculationStorageResultValueDto> storageResultValueDtoListSorted = taskCalculationStorageResultDto.getStorageResultValues().stream()
                .sorted(Comparator.comparing(v -> dateTimeMapper.dateTimeAsLocalDateTime(v.getDateTimeStart())))
                .toList();

        for (TaskCalculationStorageResultValueDto storageResultValueDto : storageResultValueDtoListSorted) {
            StorageResultValue storageResultValue = taskCalculationResultMapper.mapTaskCalculationStorageResultValueDtoToStorageResultValue(storageResultValueDto);
            storageResultValue.setStorageResult(storageResult);
            storageResultValues.add(storageResultValue);
        }

        storageResult.setStorageResultValues(storageResultValues);
        return storageResult;
    }

    private MovableDemandResult mapTaskCalculationMovableDemandResultDtoToMovableDemandResult(TaskCalculationResultDto taskCalculationResult, TaskCalculationMovableDemandResultDto movableDemandResultDto, TaskResult taskResult) {

        MovableDemandRevision movableDemandRevision = taskRepository.getMovableDemandRevisionByTaskIdAndStorageId(
                taskCalculationResult.getId(),
                movableDemandResultDto.getId()
        );

        MovableDemandResult movableDemandResult = new MovableDemandResult();
        movableDemandResult.setMovableDemandRevision(movableDemandRevision);
        movableDemandResult.setTaskResult(taskResult);

        List<TaskCalculationMovableDemandResultValueDto> movableDemandResultValueDtoListSorted = movableDemandResultDto.getMovableDemandResultValues().stream()
                .sorted(Comparator.comparing(v -> dateTimeMapper.dateTimeAsLocalDateTime(v.getDateTimeStart())))
                .toList();

        List<MovableDemandResultValue> movableDemandResultValues = new ArrayList<>();
        for (TaskCalculationMovableDemandResultValueDto movableDemandResultValueDto : movableDemandResultValueDtoListSorted) {
            MovableDemandResultValue movableDemandResultValue = taskCalculationResultMapper.mapTaskCalculationMovableDemandResultValueDtoToMovableDemandResultValue(movableDemandResultValueDto);
            movableDemandResultValue.setMovableDemandResult(movableDemandResult);
            movableDemandResultValues.add(movableDemandResultValue);
        }
        movableDemandResult.setMovableDemandResultValues(movableDemandResultValues);

        return movableDemandResult;
    }
}
