package com.github.kacperpotapczyk.pvoptimizer.backend.controller;

import com.github.kacperpotapczyk.pvoptimizer.backend.dto.storage.StorageDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.dto.storage.StorageRevisionDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.dto.storage.StorageRevisionsListDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.storage.Storage;
import com.github.kacperpotapczyk.pvoptimizer.backend.mapper.StorageMapper;
import com.github.kacperpotapczyk.pvoptimizer.backend.service.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("api/storages")
public class StorageController {

    private final StorageService storageService;
    private final StorageMapper storageMapper;

    @Autowired
    public StorageController(StorageService storageService, StorageMapper storageMapper) {
        this.storageService = storageService;
        this.storageMapper = storageMapper;
    }

    @GetMapping("/list")
    @Operation(summary = "Returns list of all Storages with their revisions")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = StorageRevisionsListDto.class))})
    })
    public Page<StorageRevisionsListDto> getStorageListWithRevisions(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "25") int pageSize) {

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("id").ascending());
        Page<Storage> storagePage = storageService.getBaseObjects(pageable);
        return storagePage.map(storageMapper::mapStorageToStorageRevisionsListDto);
    }

    @GetMapping("/{name}")
    @Operation(summary = "Returns Storage by its name")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Storage found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = StorageDto.class))}
            ),
            @ApiResponse(responseCode = "404", description = "Storage not found", content = @Content)
    })
    public StorageDto getStorageWithAllRevisions(
            @PathVariable String name,
            @RequestParam(value = "revisionNumber", required = false) Long revisionNumber) {

        try {
            if (revisionNumber != null) {
                return storageMapper.mapStorageToStorageDto(storageService.getBaseObjectWithRevision(name, revisionNumber));
            }
            else {
                return storageMapper.mapStorageToStorageDto(storageService.findByName(name));
            }
        } catch (ObjectNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @PostMapping("")
    @Operation(summary = "Creates new storage with one revision")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Storage created",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = StorageDto.class))}
            ),
            @ApiResponse(responseCode = "400", description = "Storage could not be created", content = @Content)
    })
    public StorageDto newStorage(@RequestBody StorageDto storageDto) {

        try {
            return storageMapper.mapStorageToStorageDto(storageService.newBaseObject(storageMapper.mapStorageDtoToStorage(storageDto)));
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

    @PostMapping("/{name}")
    @Operation(summary = "Adds new revision to existing storage")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Revision added",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = StorageDto.class))}
            ),
            @ApiResponse(responseCode = "404", description = "Storage not found", content = @Content)
    })
    public StorageDto addRevision(@RequestBody StorageRevisionDto storageRevisionDto, @PathVariable String name) {

        try {
            return storageMapper.mapStorageToStorageDto(
                    storageService.addRevision(name, storageMapper.mapStorageRevisionDtoToStorageRevision(storageRevisionDto))
            );
        } catch (ObjectNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @DeleteMapping("/{name}")
    @Operation(summary = "Deletes storage with all revisions")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Storage deleted",
                    content = @Content
            ),
            @ApiResponse(responseCode = "404", description = "Storage not found", content = @Content)
    })
    public void deleteStorage(@PathVariable String name) {

        try {
            storageService.deleteBaseObject(name);
        } catch (ObjectNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @DeleteMapping("/{name}/{revisionNumber}")
    @Operation(summary = "Deletes revision of storage")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Revision deleted",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = StorageDto.class))}
            ),
            @ApiResponse(responseCode = "400", description = "Could not delete last revision of given storage", content = @Content),
            @ApiResponse(responseCode = "404", description = "Storage or revision not found", content = @Content)
    })
    public StorageDto deleteStorageRevision(@PathVariable String name, @PathVariable Long revisionNumber) {

        try {
            return storageMapper.mapStorageToStorageDto(storageService.deleteBaseObjectRevision(name, revisionNumber));
        } catch (ObjectNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        } catch (IllegalStateException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }
}
