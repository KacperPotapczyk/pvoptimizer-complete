package com.github.kacperpotapczyk.pvoptimizer.backend.mapper;

import com.github.kacperpotapczyk.pvoptimizer.backend.dto.contract.*;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.contract.*;
import com.github.kacperpotapczyk.pvoptimizer.backend.service.TariffService;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class ContractMapper {

    @Autowired
    TariffService tariffService;

    public abstract ContractRevisionsListDto mapContractToContractRevisionsListDto(Contract contract);
    public abstract ContractRevisionListDto mapRevisionsToRevisionListDto(ContractRevision contractRevision);

    public abstract ContractDto mapContractToContractDto(Contract contract);
    @Mapping(target = "tariff", expression = "java(tariffService.findByName(contractDto.tariff().name()))")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    public abstract Contract mapContractDtoToContract(ContractDto contractDto);

    public abstract ContractRevisionDto mapContractRevisionToContractRevisionDto(ContractRevision contractRevision);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "contract", ignore = true)
    public abstract ContractRevision mapContractRevisionDtoToContractRevision(ContractRevisionDto contractRevisionDto);
}
