package com.github.kacperpotapczyk.pvoptimizer.backend.controller;

import com.github.kacperpotapczyk.pvoptimizer.backend.dto.demand.DemandDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.dto.demand.DemandRevisionDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.dto.demand.DemandRevisionsListDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.demand.Demand;
import com.github.kacperpotapczyk.pvoptimizer.backend.mapper.DemandMapper;
import com.github.kacperpotapczyk.pvoptimizer.backend.service.DemandService;
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
@RequestMapping("/api/demands")
public class DemandController {

    private final DemandService demandService;
    private final DemandMapper demandMapper;

    @Autowired
    public DemandController(DemandService demandService, DemandMapper demandMapper) {
        this.demandService = demandService;
        this.demandMapper = demandMapper;
    }

    @GetMapping("/list")
    @Operation(summary = "Returns list of all Demands with their revisions")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DemandRevisionsListDto.class))})
    })
    public Page<DemandRevisionsListDto> getDemandListWithRevisions(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "25") int pageSize) {

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("id").ascending());
        Page<Demand> demands = demandService.getBaseObjects(pageable);
        return demands.map(demandMapper::mapDemandToDemandRevisionsListDto);
    }


    @GetMapping("/{name}")
    @Operation(summary = "Returns Demand by its name")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Demand found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DemandDto.class))}
            ),
            @ApiResponse(responseCode = "404", description = "Demand not found", content = @Content)
    })
    public DemandDto getDemandWithAllRevisions(
            @PathVariable String name,
            @RequestParam(value = "revisionNumber", required = false) Long revisionNumber) {

        try {
            if (revisionNumber != null) {
                return demandMapper.mapDemandToDemandDto(demandService.getBaseObjectWithRevision(name, revisionNumber));
            }
            else {
                return demandMapper.mapDemandToDemandDto(demandService.findByName(name));
            }
        } catch (ObjectNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @PostMapping("")
    @Operation(summary = "Creates new demand with one revision")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Demand created",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DemandDto.class))}
            ),
            @ApiResponse(responseCode = "400", description = "Demand could not be created", content = @Content)
    })
    public DemandDto newDemand(@RequestBody DemandDto demandDto) {

        try {
            return demandMapper.mapDemandToDemandDto(demandService.newBaseObject(demandMapper.mapDemandDtoToDemand(demandDto)));
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

    @PostMapping("/{name}")
    @Operation(summary = "Adds new revision to existing demand")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Revision added",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DemandDto.class))}
            ),
            @ApiResponse(responseCode = "404", description = "Demand not found", content = @Content)
    })
    public DemandDto addRevision(@RequestBody DemandRevisionDto demandRevisionDto, @PathVariable String name) {

        try {
            return demandMapper.mapDemandToDemandDto(
                    demandService.addRevision(name, demandMapper.mapDemandRevisionDtoToDemandRevision(demandRevisionDto))
            );
        } catch (ObjectNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @DeleteMapping("/{name}")
    @Operation(summary = "Deletes demand with all revisions")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Demand deleted",
                    content = @Content
            ),
            @ApiResponse(responseCode = "404", description = "Demand not found", content = @Content)
    })
    public void deleteDemand(@PathVariable String name) {

        try {
            demandService.deleteBaseObject(name);
        } catch (ObjectNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @DeleteMapping("/{name}/{revisionNumber}")
    @Operation(summary = "Deletes revision of demand")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Revision deleted",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DemandDto.class))}
            ),
            @ApiResponse(responseCode = "400", description = "Could not delete last revision of given demand", content = @Content),
            @ApiResponse(responseCode = "404", description = "Demand or revision not found", content = @Content)
    })
    public DemandDto deleteDemandRevision(@PathVariable String name, @PathVariable Long revisionNumber) {

        try {
            return demandMapper.mapDemandToDemandDto(demandService.deleteBaseObjectRevision(name, revisionNumber));
        } catch (ObjectNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        } catch (IllegalStateException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }
}
