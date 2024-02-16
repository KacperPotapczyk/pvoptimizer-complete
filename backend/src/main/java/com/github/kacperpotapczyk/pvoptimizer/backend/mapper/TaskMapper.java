package com.github.kacperpotapczyk.pvoptimizer.backend.mapper;

import com.github.kacperpotapczyk.pvoptimizer.backend.dto.task.TaskBaseObjectRevisionDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.dto.task.TaskDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.contract.ContractRevision;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.demand.DemandRevision;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.production.ProductionRevision;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.storage.StorageRevision;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.tariff.TariffRevision;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.task.Task;
import com.github.kacperpotapczyk.pvoptimizer.backend.service.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class TaskMapper {

    @Autowired
    DemandService demandService;

    @Autowired
    ProductionService productionService;

    @Autowired
    TariffService tariffService;

    @Autowired
    ContractService contractService;
    @Autowired
    StorageService storageService;

    public abstract TaskDto mapTaskToTaskDto(Task task);
    @Mapping(source = "demand.name", target = "baseName")
    public abstract TaskBaseObjectRevisionDto mapDemandRevisionToTaskBaseObjectRevisionDto(DemandRevision demandRevision);
    @Mapping(source = "production.name", target = "baseName")
    public abstract TaskBaseObjectRevisionDto mapProductionRevisionToTaskBaseObjectRevisionDto(ProductionRevision productionRevision);
    @Mapping(source = "tariff.name", target = "baseName")
    public abstract TaskBaseObjectRevisionDto mapTariffRevisionToTaskBaseObjectRevisionDto(TariffRevision tariffRevision);
    @Mapping(source = "contract.name", target = "baseName")
    public abstract TaskBaseObjectRevisionDto mapContractRevisionToTaskBaseObjectRevisionDto(ContractRevision contractRevision);
    @Mapping(source = "storage.name", target = "baseName")
    public abstract TaskBaseObjectRevisionDto mapStorageRevisionToTaskBaseObjectRevisionDto(StorageRevision storageRevision);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "readOnly", ignore = true)
    @Mapping(target = "createdDateTime", ignore = true)
    @Mapping(target = "updateDateTime", ignore = true)
    public abstract Task mapTaskDtoToTask(TaskDto taskDto);

    public DemandRevision mapTaskBaseObjectRevisionDtoToDemandRevision(TaskBaseObjectRevisionDto dto) {
        return demandService.getBaseObjectRevision(dto.baseName(), dto.revisionNumber());
    }

    public ProductionRevision mapTaskBaseObjectRevisionDtoToProductionRevision(TaskBaseObjectRevisionDto dto) {
        return productionService.getBaseObjectRevision(dto.baseName(), dto.revisionNumber());
    }

    public TariffRevision mapTaskBaseObjectRevisionDtoToTariffRevision(TaskBaseObjectRevisionDto dto) {
        return tariffService.getBaseObjectRevision(dto.baseName(), dto.revisionNumber());
    }

    public ContractRevision mapTaskBaseObjectRevisionDtoToContractRevision(TaskBaseObjectRevisionDto dto) {
        return contractService.getBaseObjectRevision(dto.baseName(), dto.revisionNumber());
    }

    public StorageRevision mapTaskBaseObjectRevisionDtoToStorageRevision(TaskBaseObjectRevisionDto dto) {
        return storageService.getBaseObjectRevision(dto.baseName(), dto.revisionNumber());
    }
}
