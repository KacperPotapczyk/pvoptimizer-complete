package com.github.kacperpotapczyk.pvoptimizer.backend.mapper;

import com.github.kacperpotapczyk.pvoptimizer.backend.dto.result.ContractResultDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.dto.result.MovableDemandResultDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.dto.result.StorageResultDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.dto.result.TaskResultDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.result.ContractResult;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.result.MovableDemandResult;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.result.StorageResult;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.result.TaskResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskResultMapper {

    @Mapping(source = "task.name", target = "taskName")
    TaskResultDto mapTaskResultToTaskResultDto(TaskResult taskResult);

    @Mapping(target = "contractName", source = "contractRevision.contract.name")
    @Mapping(target = "revisionNumber", source = "contractRevision.revisionNumber")
    ContractResultDto mapContractResultToContractResultsDto(ContractResult contractResult);

    @Mapping(target = "storageName", source = "storageRevision.storage.name")
    @Mapping(target = "revisionNumber", source = "storageRevision.revisionNumber")
    StorageResultDto mapStorageResultToStorageResultDto(StorageResult storageResult);

    @Mapping(target = "movableDemandName", source = "movableDemandRevision.movableDemand.name")
    @Mapping(target = "revisionNumber", source = "movableDemandRevision.revisionNumber")
    MovableDemandResultDto mapMovableDemandResultToMovableDemandResultDto(MovableDemandResult movableDemandResult);
}
