package com.github.kacperpotapczyk.pvoptimizer.backend.controller;

import com.github.kacperpotapczyk.pvoptimizer.backend.dto.movabledemand.MovableDemandDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.dto.movabledemand.MovableDemandRevisionDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.dto.movabledemand.MovableDemandRevisionsListDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.movabledemand.MovableDemand;
import com.github.kacperpotapczyk.pvoptimizer.backend.mapper.MovableDemandMapper;
import com.github.kacperpotapczyk.pvoptimizer.backend.service.MovableDemandService;
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
@RequestMapping("api/movable-demands")
public class MovableDemandController {

    private final MovableDemandService movableDemandService;
    private final MovableDemandMapper mapper;

    @Autowired
    public MovableDemandController(MovableDemandService movableDemandService, MovableDemandMapper mapper) {
        this.movableDemandService = movableDemandService;
        this.mapper = mapper;
    }

    @GetMapping("/list")
    @Operation(summary = "Returns list of all Movable Demands with their revisions")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MovableDemandRevisionsListDto.class))})
    })
    public Page<MovableDemandRevisionsListDto> getMovableDemandListWithRevisions(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "25") int pageSize) {

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("id").ascending());
        Page<MovableDemand> movableDemandPage = movableDemandService.getBaseObjects(pageable);
        return movableDemandPage.map(mapper::mapMovableDemandToMovableDemandRevisionsListDto);
    }

    @GetMapping("/{name}")
    @Operation(summary = "Returns Movable Demand by its name")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Movable Demand found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MovableDemandDto.class))}
            ),
            @ApiResponse(responseCode = "404", description = "Movable Demand not found", content = @Content)
    })
    public MovableDemandDto getMovableDemandWithAllRevisions(
            @PathVariable String name,
            @RequestParam(value = "revisionNumber", required = false) Long revisionNumber) {

        try {
            if (revisionNumber != null) {
                return mapper.mapMovableDemandToMovableDemandDto(movableDemandService.getBaseObjectWithRevision(name, revisionNumber));
            }
            else {
                return mapper.mapMovableDemandToMovableDemandDto(movableDemandService.findByName(name));
            }
        } catch (ObjectNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @PostMapping("")
    @Operation(summary = "Creates new Movable Demand with one revision")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Movable Demand created",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MovableDemandDto.class))}
            ),
            @ApiResponse(responseCode = "400", description = "Movable Demand could not be created", content = @Content)
    })
    public MovableDemandDto newStorage(@RequestBody MovableDemandDto movableDemandDto) {

        try {
            return mapper.mapMovableDemandToMovableDemandDto(movableDemandService.newBaseObject(mapper.mapMovableDemandDtoToMovableDemand(movableDemandDto)));
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

    @PostMapping("/{name}")
    @Operation(summary = "Adds new revision to existing Movable Demand")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Revision added",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MovableDemandDto.class))}
            ),
            @ApiResponse(responseCode = "404", description = "Movable Demand not found", content = @Content)
    })
    public MovableDemandDto addRevision(@RequestBody MovableDemandRevisionDto movableDemandRevisionDto, @PathVariable String name) {

        try {
            return mapper.mapMovableDemandToMovableDemandDto(
                    movableDemandService.addRevision(
                            name,
                            mapper.mapMovableDemandRevisionDtoToMovableDemandRevision(movableDemandRevisionDto)
                    ));
        } catch (ObjectNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @DeleteMapping("/{name}")
    @Operation(summary = "Deletes Movable Demand with all revisions")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Movable Demand deleted",
                    content = @Content
            ),
            @ApiResponse(responseCode = "404", description = "Movable Demand not found", content = @Content)
    })
    public void deleteStorage(@PathVariable String name) {

        try {
            movableDemandService.deleteBaseObject(name);
        } catch (ObjectNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @DeleteMapping("/{name}/{revisionNumber}")
    @Operation(summary = "Deletes revision of Movable Demand")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Revision deleted",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = MovableDemandDto.class))}
            ),
            @ApiResponse(responseCode = "400", description = "Could not delete last revision of given Movable Demand", content = @Content),
            @ApiResponse(responseCode = "404", description = "Movable Demand or revision not found", content = @Content)
    })
    public MovableDemandDto deleteStorageRevision(@PathVariable String name, @PathVariable Long revisionNumber) {

        try {
            return mapper.mapMovableDemandToMovableDemandDto(movableDemandService.deleteBaseObjectRevision(name, revisionNumber));
        } catch (ObjectNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        } catch (IllegalStateException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }
}
