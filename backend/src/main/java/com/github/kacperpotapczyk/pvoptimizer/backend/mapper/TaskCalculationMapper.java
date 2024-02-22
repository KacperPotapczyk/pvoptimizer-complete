package com.github.kacperpotapczyk.pvoptimizer.backend.mapper;

import com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.*;
import com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskContractConstraintDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.constraint.TimeWindowConstraint;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.contract.ContractRevision;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.contract.ContractType;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.cyclicalvalue.CyclicalDailyValue;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.demand.DemandRevision;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.demand.DemandValue;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.production.ProductionRevision;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.production.ProductionValue;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.storage.Storage;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.storage.StorageRevision;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.tariff.TariffRevision;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.task.Task;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = DateTimeMapper.class)
public abstract class TaskCalculationMapper {

    public abstract TaskContractTypeDto mapContractTypeToTaskContractTypeDto(ContractType contractType);

    public abstract TaskContractConstraintDto mapContractConstraintToTaskContractConstraintDto(TimeWindowConstraint timeWindowConstraint);

    public abstract TaskStorageConstraintDto mapStorageConstraintToTaskStorageConstraintDto(TimeWindowConstraint timeWindowConstraint);

    public abstract TaskDemandValueDto mapDemandValueToTaskDemandValueDto(DemandValue demandValue);

    public abstract TaskProductionValueDto mapProductionValueToTaskProductionValueDto(ProductionValue productionValue);

    public abstract CyclicalDailyValueDto mapCyclicalDailyValueToCyclicalDailyValueDto(CyclicalDailyValue cyclicalDailyValue);

    public TaskCalculationDto mapTaskToTaskCalculationDto(Task task) {

        DateTimeMapper dateTimeMapper = new DateTimeMapper();
        LocalDateTime taskDateTimeStart = task.getDateTimeStart();
        LocalDateTime taskDateTimeEnd = task.getDateTimeEnd();

        return new TaskCalculationDto(
                task.getId(),
                dateTimeMapper.dateTimeAsCharSequence(taskDateTimeStart),
                dateTimeMapper.dateTimeAsCharSequence(task.getDateTimeEnd()),
                task.getContractRevisions().stream()
                        .map(contractRevision -> this.mapContractRevisionToTaskContractDto(contractRevision, taskDateTimeStart, taskDateTimeEnd))
                        .toList(),
                task.getDemandRevisions().stream()
                        .map(demandRevision -> this.mapDemandRevisionToTaskDemandDto(demandRevision, taskDateTimeStart, taskDateTimeEnd))
                        .toList(),
                task.getProductionRevisions().stream()
                        .map(productionRevision -> this.mapProductionRevisionToTaskProductionDto(productionRevision, taskDateTimeStart, taskDateTimeEnd))
                        .toList(),
                task.getTariffRevisions().stream()
                        .map(this::mapTariffRevisionToTaskTariffDto)
                        .toList(),
                task.getStorageRevisions().stream()
                        .map(storageRevision -> this.mapStorageRevisionToTaskStorageDto(storageRevision, taskDateTimeStart, taskDateTimeEnd))
                        .toList()
        );
    }

    private TaskContractDto mapContractRevisionToTaskContractDto(ContractRevision contractRevision, LocalDateTime windowStart, LocalDateTime windowEnd) {

        return new TaskContractDto(
                contractRevision.getContract().getId(),
                contractRevision.getContract().getName(),
                contractRevision.getRevisionNumber(),
                this.mapContractTypeToTaskContractTypeDto(contractRevision.getContract().getContractType()),
                contractRevision.getContract().getTariff().getName(),
                contractRevision.getMinPowerConstraintsInTimeWindow(windowStart, windowEnd).stream().map(this::mapContractConstraintToTaskContractConstraintDto).collect(Collectors.toList()),
                contractRevision.getMaxPowerConstraintsInTimeWindow(windowStart, windowEnd).stream().map(this::mapContractConstraintToTaskContractConstraintDto).collect(Collectors.toList()),
                contractRevision.getMinEnergyConstraintsInTimeWindow(windowStart, windowEnd).stream().map(this::mapContractConstraintToTaskContractConstraintDto).collect(Collectors.toList()),
                contractRevision.getMaxEnergyConstraintsInTimeWindow(windowStart, windowEnd).stream().map(this::mapContractConstraintToTaskContractConstraintDto).collect(Collectors.toList())
        );
    }

    private TaskStorageDto mapStorageRevisionToTaskStorageDto(StorageRevision storageRevision, LocalDateTime windowStart, LocalDateTime windowEnd) {

        Storage storage = storageRevision.getStorage();
        return new TaskStorageDto(
                storage.getId(),
                storage.getName(),
                storageRevision.getRevisionNumber(),
                storage.getCapacity(),
                storage.getMaxCharge(),
                storage.getMaxDischarge(),
                storageRevision.getInitialEnergy(),
                storageRevision.getMinChargeConstraintsInTimeWindow(windowStart, windowEnd).stream().map(this::mapStorageConstraintToTaskStorageConstraintDto).toList(),
                storageRevision.getMaxChargeConstraintsInTimeWindow(windowStart, windowEnd).stream().map(this::mapStorageConstraintToTaskStorageConstraintDto).toList(),
                storageRevision.getMinDischargeConstraintsInTimeWindow(windowStart, windowEnd).stream().map(this::mapStorageConstraintToTaskStorageConstraintDto).toList(),
                storageRevision.getMaxDischargeConstraintsInTimeWindow(windowStart, windowEnd).stream().map(this::mapStorageConstraintToTaskStorageConstraintDto).toList(),
                storageRevision.getMinEnergyConstraintsInTimeWindow(windowStart, windowEnd).stream().map(this::mapStorageConstraintToTaskStorageConstraintDto).toList(),
                storageRevision.getMaxEnergyConstraintsInTimeWindow(windowStart, windowEnd).stream().map(this::mapStorageConstraintToTaskStorageConstraintDto).toList()
        );
    }

    private TaskDemandDto mapDemandRevisionToTaskDemandDto(DemandRevision demandRevision, LocalDateTime windowStart, LocalDateTime windowEnd) {

        return new TaskDemandDto(
                demandRevision.getDemand().getId(),
                demandRevision.getDemand().getName(),
                demandRevision.getRevisionNumber(),
                demandRevision.getDemandValuesForTimeWindow(windowStart, windowEnd).stream()
                        .toList().stream()
                        .sorted(Comparator.comparing(DemandValue::getDateTime))
                        .map(this::mapDemandValueToTaskDemandValueDto)
                        .toList()
        );
    }

    private TaskProductionDto mapProductionRevisionToTaskProductionDto(ProductionRevision productionRevision, LocalDateTime windowStart, LocalDateTime windowEnd) {

        return new TaskProductionDto(
                productionRevision.getProduction().getId(),
                productionRevision.getProduction().getName(),
                productionRevision.getRevisionNumber(),
                productionRevision.getProductionValuesForTimeWindow(windowStart, windowEnd).stream()
                        .toList().stream()
                        .sorted(Comparator.comparing(ProductionValue::getDateTime))
                        .map(this::mapProductionValueToTaskProductionValueDto)
                        .toList()
        );
    }

    private TaskTariffDto mapTariffRevisionToTaskTariffDto(TariffRevision tariffRevision) {

        return new TaskTariffDto(
                tariffRevision.getTariff().getId(),
                tariffRevision.getTariff().getName(),
                tariffRevision.getRevisionNumber(),
                tariffRevision.getDefaultPrice(),
                tariffRevision.getCyclicalDailyValues().stream()
                        .map(this::mapCyclicalDailyValueToCyclicalDailyValueDto)
                        .toList()
        );
    }
}
