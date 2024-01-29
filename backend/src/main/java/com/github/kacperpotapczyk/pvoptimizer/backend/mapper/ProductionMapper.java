package com.github.kacperpotapczyk.pvoptimizer.backend.mapper;

import com.github.kacperpotapczyk.pvoptimizer.backend.dto.production.*;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.production.Production;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.production.ProductionRevision;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.production.ProductionValue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductionMapper {

    ProductionRevisionsListDto mapProductionToProductionRevisionsListDto(Production production);
    ProductionRevisionListDto mapRevisionsToRevisionListDto(ProductionRevision productionRevision);

    ProductionDto mapProductionToProductionDto(Production production);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Production mapProductionDtoToProduction(ProductionDto productionDto);

    ProductionRevisionDto mapProductionRevisionToProductionRevisionDto(ProductionRevision productionRevision);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "production", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    ProductionRevision mapProductionRevisionDtoToProductionRevision(ProductionRevisionDto productionRevisionDto);

    ProductionValueDto mapProductionValueToProductionValueDto(ProductionValue productionValue);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productionRevision", ignore = true)
    ProductionValue mapProductionValueDtoToProductionValue(ProductionValueDto productionValueDto);
}
