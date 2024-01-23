package com.github.kacperpotapczyk.pvoptimizer.backend.mapper;

import com.github.kacperpotapczyk.pvoptimizer.backend.dto.demand.*;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.demand.Demand;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.demand.DemandRevision;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.demand.DemandValue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DemandMapper {

    DemandRevisionsListDto mapDemandToDemandRevisionsListDto(Demand demand);
    DemandRevisionListDto mapRevisionsToRevisionListDto(DemandRevision demandRevision);

    DemandDto mapDemandToDemandDto(Demand demand);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Demand mapDemandDtoToDemand(DemandDto demandDto);

    DemandRevisionDto mapDemandRevisionToDemandRevisionDto(DemandRevision demandRevision);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "demand", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    DemandRevision mapDemandRevisionDtoToDemandRevision(DemandRevisionDto demandRevisionDto);

    DemandValueDto mapDemandValueToDemandValueDto(DemandValue demandValue);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "demandRevision", ignore = true)
    DemandValue mapDemandValueDtoToDemandValue(DemandValueDto demandValueDto);
}
