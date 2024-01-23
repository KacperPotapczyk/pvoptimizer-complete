package com.github.kacperpotapczyk.pvoptimizer.backend.service;

import com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.result.TaskCalculationContractResultDto;
import com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.result.TaskCalculationContractResultValueDto;
import com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.result.TaskCalculationResultDto;
import com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.result.TaskCalculationResultStatusDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.result.*;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.task.Task;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class TaskResultServiceTest {

    @Autowired
    TaskResultService taskResultService;

    @Autowired
    TaskService taskService;

    @Test
    void getResultForTaskName() {

        TaskResult taskResult = taskResultService.getResultForTaskName("readOnly");

        assertEquals(1, taskResult.getId());
        assertEquals(2, taskResult.getTask().getId());
        assertEquals("readOnly", taskResult.getTask().getName());

        assertEquals(ResultStatus.VALIDATION_SUCCESSFUL, taskResult.getResultStatus());
        assertEquals(1, taskResult.getValidationMessages().size());

        ValidationMessage validationMessage = taskResult.getValidationMessages().get(0);
        assertEquals(1, validationMessage.getTaskResult().getId());
        assertEquals(ValidationMessageLevel.INFO, validationMessage.getLevel());
        assertEquals(ValidationMessageObjectType.CONTRACT, validationMessage.getObjectType());
        assertEquals("contract1", validationMessage.getObjectName());
        assertEquals(1, validationMessage.getObjectId());
        assertEquals(1, validationMessage.getObjectRevision());
        assertEquals("test message", validationMessage.getMessage());

        assertNull(taskResult.getElapsedTime());
        assertNull(taskResult.getRelativeGap());
        assertNull(taskResult.getObjectiveFunctionValue());
        assertNull(taskResult.getOptimizerMessage());
    }

    @Test
    void initiateResult() {

        Task task = taskService.findByName("toCreateResults");

        TaskResult taskResult = taskResultService.initiateResult(task);

        assertEquals("toCreateResults", taskResult.getTask().getName());
        assertEquals(ResultStatus.STARTED, taskResult.getResultStatus());

        assertNull(taskResult.getValidationMessages());
        assertNull(taskResult.getElapsedTime());
        assertNull(taskResult.getRelativeGap());
        assertNull(taskResult.getObjectiveFunctionValue());
        assertNull(taskResult.getOptimizerMessage());
    }

    @Test
    void addValidationResult() {

        List<ValidationMessage> validationMessages = new ArrayList<>(2);
        validationMessages.add(new ValidationMessage(
                ValidationMessageLevel.WARNING,
                ValidationMessageObjectType.CONTRACT,
                "contract warning",
                1L,
                1L,
                "warning message"
        ));
        validationMessages.add(new ValidationMessage(
                ValidationMessageLevel.ERROR,
                ValidationMessageObjectType.TARIFF,
                "tariff error",
                2L,
                2L,
                "error message"
        ));

        TaskResult validationTaskResult = taskResultService.addValidationResult(9, validationMessages);

        assertEquals("toAddValidationMessage", validationTaskResult.getTask().getName());
        assertEquals(2, validationTaskResult.getValidationMessages().size());
        assertEquals(ResultStatus.VALIDATION_ERROR, validationTaskResult.getResultStatus());

        ValidationMessage errorMessage = validationTaskResult.getValidationMessages().stream()
                .filter(validationMessage -> validationMessage.getLevel() == ValidationMessageLevel.ERROR)
                .findAny()
                .orElseThrow();

        assertEquals(ValidationMessageLevel.ERROR, errorMessage.getLevel());
        assertEquals(ValidationMessageObjectType.TARIFF, errorMessage.getObjectType());
        assertEquals("tariff error", errorMessage.getObjectName());
        assertEquals(2L, errorMessage.getObjectId());
        assertEquals(2L, errorMessage.getObjectRevision());
        assertEquals("error message", errorMessage.getMessage());

        ValidationMessage warningMessage = validationTaskResult.getValidationMessages().stream()
                .filter(validationMessage -> validationMessage.getLevel() == ValidationMessageLevel.WARNING)
                .findAny()
                .orElseThrow();

        assertEquals(ValidationMessageLevel.WARNING, warningMessage.getLevel());
        assertEquals(ValidationMessageObjectType.CONTRACT, warningMessage.getObjectType());
        assertEquals("contract warning", warningMessage.getObjectName());
        assertEquals(1L, warningMessage.getObjectId());
        assertEquals(1L, warningMessage.getObjectRevision());
        assertEquals("warning message", warningMessage.getMessage());
    }

    @Test
    public void addCalculationResult() {

        TaskCalculationResultDto taskCalculationResultDto = getTaskCalculationResultDto();

        TaskResult taskResult = taskResultService.addCalculationResult(taskCalculationResultDto);

        assertEquals("toAddCalculationResult", taskResult.getTask().getName());
        assertEquals(ResultStatus.SOLUTION_FOUND, taskResult.getResultStatus());
        assertEquals(100.0, taskResult.getObjectiveFunctionValue());
        assertEquals(1.2, taskResult.getElapsedTime());
        assertEquals(0.1, taskResult.getRelativeGap());
        assertEquals("Solution found", taskResult.getOptimizerMessage());

        assertEquals(1, taskResult.getContractResults().size());
        ContractResult contractResult = taskResult.getContractResults().get(0);

        assertEquals(1, contractResult.getContractRevision().getId());
        assertEquals(1, contractResult.getContractRevision().getContract().getId());
        assertEquals(3, contractResult.getTaskResult().getId());

        assertEquals(2, contractResult.getContractResultValues().size());
        List<ContractResultValue> contractResultValues = contractResult.getContractResultValues().stream()
                .sorted(Comparator.comparing(ContractResultValue::getDateTimeStart))
                .toList();

        ContractResultValue contractResultValue1 = contractResultValues.get(0);
        assertEquals(LocalDateTime.parse("2023-01-01T10:00:00"), contractResultValue1.getDateTimeStart());
        assertEquals(LocalDateTime.parse("2023-01-01T10:15:00"), contractResultValue1.getDateTimeEnd());
        assertEquals(10.0, contractResultValue1.getPower());
        assertEquals(2.5, contractResultValue1.getEnergy());
        assertEquals(0.25, contractResultValue1.getCost());

        ContractResultValue contractResultValue2 = contractResultValues.get(1);
        assertEquals(LocalDateTime.parse("2023-01-01T10:15:00"), contractResultValue2.getDateTimeStart());
        assertEquals(LocalDateTime.parse("2023-01-01T10:30:00"), contractResultValue2.getDateTimeEnd());
        assertEquals(20.0, contractResultValue2.getPower());
        assertEquals(5.0, contractResultValue2.getEnergy());
        assertEquals(0.5, contractResultValue2.getCost());
    }

    @Test
    public void addCalculationResultToInvalidTask() {

        TaskCalculationResultDto taskCalculationResultDto = getTaskCalculationResultDto();
        taskCalculationResultDto.setId(11L);

        assertThrows(IllegalStateException.class, () -> taskResultService.addCalculationResult(taskCalculationResultDto));
    }

    @Test
    public void addCalculationResultSolutionNotFound() {

        TaskCalculationResultDto.Builder resultBuilder = TaskCalculationResultDto.newBuilder();

        resultBuilder.setId(12L);
        resultBuilder.setResultStatus(TaskCalculationResultStatusDto.SOLUTION_NOT_FOUND);
        resultBuilder.setOptimizerMessage("Invalid constraints");
        resultBuilder.setDateTimeStart("2023-01-01T10:00:00");
        resultBuilder.setDateTimeEnd("2023-01-01T10:30:00");

        TaskResult taskResult = taskResultService.addCalculationResult(resultBuilder.build());

        assertEquals("addResultSolutionNotFound", taskResult.getTask().getName());
        assertEquals(ResultStatus.SOLUTION_NOT_FOUND, taskResult.getResultStatus());
        assertNull(taskResult.getObjectiveFunctionValue());
        assertNull(taskResult.getElapsedTime());
        assertNull(taskResult.getRelativeGap());
        assertEquals("Invalid constraints", taskResult.getOptimizerMessage());
        assertEquals(0, taskResult.getContractResults().size());
    }

    private TaskCalculationResultDto getTaskCalculationResultDto() {
        TaskCalculationResultDto.Builder resultBuilder = TaskCalculationResultDto.newBuilder();

        resultBuilder.setId(10L);
        resultBuilder.setResultStatus(TaskCalculationResultStatusDto.SOLUTION_FOUND);
        resultBuilder.setObjectiveFunctionValue(100.0);
        resultBuilder.setElapsedTime(1.2);
        resultBuilder.setRelativeGap(0.1);
        resultBuilder.setOptimizerMessage("Solution found");
        resultBuilder.setDateTimeStart("2023-01-01T10:00:00");
        resultBuilder.setDateTimeEnd("2023-01-01T10:30:00");

        TaskCalculationContractResultDto.Builder contractResultBuilder = TaskCalculationContractResultDto.newBuilder();
        contractResultBuilder.setId(1L);

        List<TaskCalculationContractResultValueDto> contractResultValueDtoList = new ArrayList<>();
        contractResultValueDtoList.add(new TaskCalculationContractResultValueDto(
                "2023-01-01T10:00:00",
                "2023-01-01T10:15:00",
                10.0,
                2.5,
                0.25
        ));
        contractResultValueDtoList.add(new TaskCalculationContractResultValueDto(
                "2023-01-01T10:15:00",
                "2023-01-01T10:30:00",
                20.0,
                5.0,
                0.5
        ));
        contractResultBuilder.setContractResultValues(contractResultValueDtoList);

        List<TaskCalculationContractResultDto> taskCalculationContractResultDtoList = new ArrayList<>();
        taskCalculationContractResultDtoList.add(contractResultBuilder.build());

        resultBuilder.setContractResults(taskCalculationContractResultDtoList);

        return resultBuilder.build();
    }
}