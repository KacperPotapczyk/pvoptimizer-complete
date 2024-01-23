package com.github.kacperpotapczyk.pvoptimizer.backend.mapper;

import com.github.kacperpotapczyk.pvoptimizer.backend.dto.tariff.TariffDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.dto.tariff.TariffRevisionDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.dto.tariff.TariffRevisionListDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.dto.tariff.TariffRevisionsListDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.tariff.Tariff;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.tariff.TariffRevision;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TariffMapper {

    TariffRevisionsListDto mapTariffToTariffRevisionsListDto(Tariff tariff);
    TariffRevisionListDto mapRevisionsToRevisionListDto(TariffRevision tariffRevision);

    TariffDto mapTariffToTariffDto(Tariff tariff);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Tariff mapTariffDtoToTariff(TariffDto tariffDto);

    TariffRevisionDto mapTariffRevisionToTariffRevisionDto(TariffRevision tariffRevision);
    @Mapping(target = "tariff", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    TariffRevision mapTariffRevisionDtoToTariffRevision(TariffRevisionDto tariffRevisionDto);
}
