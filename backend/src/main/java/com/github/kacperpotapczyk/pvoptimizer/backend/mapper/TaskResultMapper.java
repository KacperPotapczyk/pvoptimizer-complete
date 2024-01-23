package com.github.kacperpotapczyk.pvoptimizer.backend.mapper;

import com.github.kacperpotapczyk.pvoptimizer.backend.dto.result.ContractResultDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.dto.result.TaskResultDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.result.ContractResult;
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
}
