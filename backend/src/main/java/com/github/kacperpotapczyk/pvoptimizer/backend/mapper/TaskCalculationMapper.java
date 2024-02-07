package com.github.kacperpotapczyk.pvoptimizer.backend.mapper;

import com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.*;
import com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskContractConstraintDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.contract.ContractConstraint;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.contract.ContractRevision;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.contract.ContractType;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.demand.DemandRevision;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.demand.DemandValue;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.production.ProductionRevision;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.production.ProductionValue;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.tariff.TariffRevision;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.task.Task;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = DateMapper.class)
public abstract class TaskCalculationMapper {

    public abstract TaskContractTypeDto mapContractTypeToTaskContractTypeDto(ContractType contractType);

    public abstract TaskContractConstraintDto mapContractConstraintToTaskContractConstraintDto(ContractConstraint contractConstraint);

    public abstract TaskDemandValueDto mapDemandValueToTaskDemandValueDto(DemandValue demandValue);

    public abstract TaskProductionValueDto mapProductionValueToTaskProductionValueDto(ProductionValue productionValue);

    public TaskCalculationDto mapTaskToTaskCalculationDto(Task task) {

        LocalDateTime taskDateTimeStart = task.getDateTimeStart();
        LocalDateTime taskDateTimeEnd = task.getDateTimeEnd();

        return new TaskCalculationDto(
                task.getId(),
                task.getDateTimeStart().toString(),
                task.getDateTimeEnd().toString(),
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

    private TaskDemandDto mapDemandRevisionToTaskDemandDto(DemandRevision demandRevision, LocalDateTime windowStart, LocalDateTime windowEnd) {

        return new TaskDemandDto(
                demandRevision.getDemand().getId(),
                demandRevision.getDemand().getName(),
                demandRevision.getRevisionNumber(),
                demandRevision.getDemandValuesInTimeWindow(windowStart, windowEnd).stream()
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
                productionRevision.getProductionValuesInTimeWindow(windowStart, windowEnd).stream()
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
                tariffRevision.getDefaultPrice()
        );
    }
}
