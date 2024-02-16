package com.github.kacperpotapczyk.pvoptimizer.backend.mapper;

import com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.*;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.task.Task;
import com.github.kacperpotapczyk.pvoptimizer.backend.service.TaskService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class TaskCalculationMapperTest {

    private final TaskService taskService;
    private final TaskCalculationMapper taskCalculationMapper;
    private final DateMapper dateMapper;

    @Autowired
    TaskCalculationMapperTest(TaskService taskService, TaskCalculationMapper taskCalculationMapper, DateMapper dateMapper) {
        this.taskService = taskService;
        this.taskCalculationMapper = taskCalculationMapper;
        this.dateMapper = dateMapper;
    }

    @Test
    @Transactional
    void mapTaskToTaskCalculationDto() {

        String taskName = "queryOnly";
        Task task = taskService.findByName(taskName);
        TaskCalculationDto taskCalculationDto = taskCalculationMapper.mapTaskToTaskCalculationDto(task);

        assertEquals(task.getId(), taskCalculationDto.getId());
        assertEquals(dateMapper.asCharSequence(task.getDateTimeStart()), taskCalculationDto.getDateTimeStart());
        assertEquals(dateMapper.asCharSequence(task.getDateTimeEnd()), taskCalculationDto.getDateTimeEnd());

        assertEquals(2, taskCalculationDto.getContracts().size());
        assertEquals(1, taskCalculationDto.getDemands().size());
        assertEquals(1, taskCalculationDto.getProductions().size());
        assertEquals(1, taskCalculationDto.getTariffs().size());
        assertEquals(1, taskCalculationDto.getStorages().size());

        TaskContractDto taskContractDto = taskCalculationDto.getContracts().stream()
                .filter(contractDto -> contractDto.getRevisionNumber() == 1)
                .findAny().orElseThrow();

        assertEquals(1, taskContractDto.getId());
        assertEquals("queryOnly", taskContractDto.getName());
        assertEquals(TaskContractTypeDto.PURCHASE, taskContractDto.getContractType());
        assertEquals("queryOnly", taskContractDto.getTariffName());
        assertEquals(0, taskContractDto.getMinPowerConstraints().size());
        assertEquals(0, taskContractDto.getMaxPowerConstraints().size());
        assertEquals(2, taskContractDto.getMinEnergyConstraints().size());
        assertEquals(0, taskContractDto.getMaxEnergyConstraints().size());

        TaskContractConstraintDto taskContractConstraintDto1 = taskContractDto.getMinEnergyConstraints().stream()
                .filter(constraint -> constraint.getConstraintValue() == 1.0)
                .findAny().orElseThrow();
        assertEquals("2023-12-23T17:00:00", taskContractConstraintDto1.getDateTimeStart());
        assertEquals("2023-12-24T17:00:00", taskContractConstraintDto1.getDateTimeEnd());

        TaskContractConstraintDto taskContractConstraintDto2 = taskContractDto.getMinEnergyConstraints().stream()
                .filter(constraint -> constraint.getConstraintValue() == 0.9)
                .findAny().orElseThrow();
        assertEquals("2023-12-24T15:00:00", taskContractConstraintDto2.getDateTimeStart());
        assertEquals("2023-12-25T19:00:00", taskContractConstraintDto2.getDateTimeEnd());

        TaskDemandDto taskDemandDto = taskCalculationDto.getDemands().get(0);
        assertEquals(1, taskDemandDto.getRevisionNumber());
        assertEquals(1, taskDemandDto.getId());
        assertEquals("queryOnly", taskDemandDto.getName());
        assertEquals(1, taskDemandDto.getDemandValues().size());

        TaskDemandValueDto taskDemandValueDto = taskDemandDto.getDemandValues().get(0);
        assertEquals(4.1, taskDemandValueDto.getValue());
        assertEquals("2023-10-05T13:00:00", taskDemandValueDto.getDateTime());

        TaskProductionDto taskProductionDto = taskCalculationDto.getProductions().get(0);
        assertEquals(1, taskProductionDto.getRevisionNumber());
        assertEquals(1, taskProductionDto.getId());
        assertEquals("queryOnly", taskProductionDto.getName());
        assertEquals(1, taskProductionDto.getProductionsValues().size());

        TaskProductionValueDto taskProductionValueDto = taskProductionDto.getProductionsValues().get(0);
        assertEquals(4.1, taskProductionValueDto.getValue());
        assertEquals("2023-10-05T13:00:00", taskProductionValueDto.getDateTime());

        TaskTariffDto taskTariffDto = taskCalculationDto.getTariffs().get(0);
        assertEquals(1, taskTariffDto.getRevisionNumber());
        assertEquals(1, taskTariffDto.getId());
        assertEquals("queryOnly", taskTariffDto.getName());
        assertEquals(0.02, taskTariffDto.getDefaultPrice());

        TaskStorageDto taskStorageDto = taskCalculationDto.getStorages().get(0);
        assertEquals(1, taskStorageDto.getRevisionNumber());
        assertEquals(1, taskStorageDto.getId());
        assertEquals("queryOnly", taskStorageDto.getName());
        assertEquals(100.0, taskStorageDto.getCapacity());
        assertEquals(10.0, taskStorageDto.getMaxCharge());
        assertEquals(20.0, taskStorageDto.getMaxDischarge());
        assertEquals(40.0, taskStorageDto.getInitialEnergy());
        assertEquals(0, taskStorageDto.getMinChargeConstraints().size());
        assertEquals(0, taskStorageDto.getMaxChargeConstraints().size());
        assertEquals(0, taskStorageDto.getMinDischargeConstraints().size());
        assertEquals(0, taskStorageDto.getMaxDischargeConstraints().size());
        assertEquals(0, taskStorageDto.getMinEnergyConstraints().size());
        assertEquals(0, taskStorageDto.getMaxEnergyConstraints().size());
    }
}