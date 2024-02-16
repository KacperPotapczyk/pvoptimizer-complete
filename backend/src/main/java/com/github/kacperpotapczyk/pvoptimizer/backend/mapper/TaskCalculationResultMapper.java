package com.github.kacperpotapczyk.pvoptimizer.backend.mapper;

import com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.result.TaskCalculationContractResultValueDto;
import com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.result.TaskCalculationResultStatusDto;
import com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.result.TaskCalculationStorageResultValueDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.result.ContractResultValue;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.result.ResultStatus;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.result.StorageResultValue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = DateMapper.class)
public interface TaskCalculationResultMapper {

    ResultStatus mapTaskCalculationResultStatusDtoToResultStatus(TaskCalculationResultStatusDto taskCalculationResultStatusDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "contractResult", ignore = true)
    ContractResultValue mapTaskCalculationContractResultValueDtoToContractResultValue(TaskCalculationContractResultValueDto taskCalculationContractResultValueDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "storageResult", ignore = true)
    StorageResultValue mapTaskCalculationStorageResultValueDtoToStorageResultValue(TaskCalculationStorageResultValueDto taskCalculationStorageResultValueDto);
}
