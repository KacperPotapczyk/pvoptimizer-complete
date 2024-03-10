package com.github.kacperpotapczyk.pvoptimizer.backend.mapper;

import com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.*;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.task.Task;
import com.github.kacperpotapczyk.pvoptimizer.backend.service.TaskService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class TaskCalculationMapperTest {

    private final TaskService taskService;
    private final TaskCalculationMapper taskCalculationMapper;
    private final DateTimeMapper dateTimeMapper;

    @Autowired
    TaskCalculationMapperTest(TaskService taskService, TaskCalculationMapper taskCalculationMapper, DateTimeMapper dateTimeMapper) {
        this.taskService = taskService;
        this.taskCalculationMapper = taskCalculationMapper;
        this.dateTimeMapper = dateTimeMapper;
    }

    @Test
    @Transactional
    void mapTaskToTaskCalculationDto() {

        String taskName = "queryOnly";
        Task task = taskService.findByName(taskName);
        TaskCalculationDto taskCalculationDto = taskCalculationMapper.mapTaskToTaskCalculationDto(task);

        assertEquals(task.getId(), taskCalculationDto.getId());
        assertEquals(dateTimeMapper.dateTimeAsCharSequence(task.getDateTimeStart()), taskCalculationDto.getDateTimeStart());
        assertEquals(dateTimeMapper.dateTimeAsCharSequence(task.getDateTimeEnd()), taskCalculationDto.getDateTimeEnd());

        assertEquals(2, taskCalculationDto.getContracts().size());
        assertEquals(1, taskCalculationDto.getDemands().size());
        assertEquals(1, taskCalculationDto.getProductions().size());
        assertEquals(1, taskCalculationDto.getTariffs().size());
        assertEquals(1, taskCalculationDto.getStorages().size());
        assertEquals(1, taskCalculationDto.getMovableDemands().size());

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
        assertEquals(2, taskTariffDto.getCyclicalDailyValues().size());

        CyclicalDailyValueDto cyclicalDailyValueDto1 = taskTariffDto.getCyclicalDailyValues().stream()
                .filter(dailyValueDto -> dailyValueDto.getDayOfTheWeek() == WeekdaysDto.MONDAY_TO_FRIDAY)
                .findAny().orElseThrow();
        assertEquals(2, cyclicalDailyValueDto1.getDailyTimeValues().size());

        List<DailyTimeValueDto> dailyTimeValueList1 = cyclicalDailyValueDto1.getDailyTimeValues();
        assertEquals(0.03, dailyTimeValueList1.get(0).getCurrentValue());
        assertEquals("06:00:00", dailyTimeValueList1.get(0).getStartTime().toString());
        assertEquals(0.01, dailyTimeValueList1.get(1).getCurrentValue());
        assertEquals("22:00:00", dailyTimeValueList1.get(1).getStartTime().toString());

        CyclicalDailyValueDto cyclicalDailyValueDto2 = taskTariffDto.getCyclicalDailyValues().stream()
                .filter(dailyValueDto -> dailyValueDto.getDayOfTheWeek() == WeekdaysDto.WEEKEND)
                .findAny().orElseThrow();
        assertEquals(2, cyclicalDailyValueDto2.getDailyTimeValues().size());

        List<DailyTimeValueDto> dailyTimeValueList2 = cyclicalDailyValueDto2.getDailyTimeValues();
        assertEquals(0.025, dailyTimeValueList2.get(0).getCurrentValue());
        assertEquals("08:00:00", dailyTimeValueList2.get(0).getStartTime().toString());
        assertEquals(0.005, dailyTimeValueList2.get(1).getCurrentValue());
        assertEquals("23:30:00", dailyTimeValueList2.get(1).getStartTime().toString());

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

        TaskMovableDemandDto taskMovableDemandDto = taskCalculationDto.getMovableDemands().get(0);
        assertEquals(1, taskMovableDemandDto.getRevisionNumber());
        assertEquals(7, taskMovableDemandDto.getId());
        assertEquals("forTask", taskMovableDemandDto.getName());
        assertEquals(2, taskMovableDemandDto.getMovableDemandStarts().size());
        assertEquals("2023-12-24T13:55:00", taskMovableDemandDto.getMovableDemandStarts().get(0).getStart());
        assertEquals("2023-12-24T15:00:00", taskMovableDemandDto.getMovableDemandStarts().get(1).getStart());
        assertEquals(2, taskMovableDemandDto.getMovableDemandValues().size());
        assertEquals(5.0, taskMovableDemandDto.getMovableDemandValues().get(0).getValue());
        assertEquals(3.0, taskMovableDemandDto.getMovableDemandValues().get(1).getValue());
    }
}