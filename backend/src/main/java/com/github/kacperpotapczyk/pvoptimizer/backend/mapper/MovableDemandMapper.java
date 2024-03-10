package com.github.kacperpotapczyk.pvoptimizer.backend.mapper;

import com.github.kacperpotapczyk.pvoptimizer.backend.dto.movabledemand.*;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.movabledemand.MovableDemand;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.movabledemand.MovableDemandRevision;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.movabledemand.MovableDemandStart;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.movabledemand.MovableDemandValue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MovableDemandMapper {

    MovableDemandRevisionsListDto mapMovableDemandToMovableDemandRevisionsListDto(MovableDemand movableDemand);
    MovableDemandRevisionListDto mapRevisionToRevisionListDto(MovableDemandRevision movableDemandRevision);

    MovableDemandDto mapMovableDemandToMovableDemandDto(MovableDemand MovableDemand);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    MovableDemand mapMovableDemandDtoToMovableDemand(MovableDemandDto movableDemandDto);

    MovableDemandRevisionDto mapMovableDemandRevisionToMovableDemandRevisionDto(MovableDemandRevision movableDemandRevision);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "movableDemand", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    MovableDemandRevision mapMovableDemandRevisionDtoToMovableDemandRevision(MovableDemandRevisionDto movableDemandRevisionDto);

    MovableDemandValueDto mapMovableDemandValueToMovableDemandValueDto(MovableDemandValue movableDemandValue);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "movableDemandRevision", ignore = true)
    MovableDemandValue mapMovableDemandValueDtoToMovableDemandValue(MovableDemandValueDto movableDemandValueDto);

    MovableDemandStartDto mapMovableDemandStartToMovableDemandStartDto(MovableDemandStart movableDemandStart);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "movableDemandRevision", ignore = true)
    MovableDemandStart mapMovableDemandStartDtoToMovableDemandStart(MovableDemandStartDto movableDemandStartDto);
}
