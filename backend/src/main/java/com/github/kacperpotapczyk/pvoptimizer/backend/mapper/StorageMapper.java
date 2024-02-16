package com.github.kacperpotapczyk.pvoptimizer.backend.mapper;

import com.github.kacperpotapczyk.pvoptimizer.backend.dto.storage.StorageDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.dto.storage.StorageRevisionDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.dto.storage.StorageRevisionListDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.dto.storage.StorageRevisionsListDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.storage.Storage;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.storage.StorageRevision;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StorageMapper {

    StorageRevisionsListDto mapStorageToStorageRevisionsListDto(Storage storage);
    StorageRevisionListDto mapRevisionsToStorageRevisionListDto(StorageRevision storageRevision);

    StorageDto mapStorageToStorageDto(Storage storage);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Storage mapStorageDtoToStorage(StorageDto storageDto);

    StorageRevisionDto mapStorageRevisionToStorageRevisionDto(StorageRevision storageRevision);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "storage", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    StorageRevision mapStorageRevisionDtoToStorageRevision(StorageRevisionDto storageRevisionDto);
}
