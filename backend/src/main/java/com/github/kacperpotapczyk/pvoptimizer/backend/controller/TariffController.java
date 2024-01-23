package com.github.kacperpotapczyk.pvoptimizer.backend.controller;

import com.github.kacperpotapczyk.pvoptimizer.backend.dto.tariff.TariffDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.dto.tariff.TariffRevisionDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.dto.tariff.TariffRevisionsListDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.tariff.Tariff;
import com.github.kacperpotapczyk.pvoptimizer.backend.mapper.TariffMapper;
import com.github.kacperpotapczyk.pvoptimizer.backend.service.TariffService;
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
@RequestMapping("/api/tariffs")
public class TariffController {

    private final TariffService tariffService;
    private final TariffMapper tariffMapper;

    @Autowired
    public TariffController(TariffService tariffService, TariffMapper tariffMapper) {
        this.tariffService = tariffService;
        this.tariffMapper = tariffMapper;
    }

    @GetMapping("/list")
    @Operation(summary = "Returns list of all Tariffs with their revisions")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = TariffRevisionsListDto.class))})
    })
    public Page<TariffRevisionsListDto> getDemandListWithRevisions(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "25") int pageSize) {

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("id").ascending());
        Page<Tariff> tariffs = tariffService.getBaseObjects(pageable);
        return tariffs.map(tariffMapper::mapTariffToTariffRevisionsListDto);
    }

    @GetMapping("/{name}")
    @Operation(summary = "Returns Tariff by its name with all revisions. If revision number is present returns Tariff with given revision")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Tariff found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = TariffDto.class))}
            ),
            @ApiResponse(responseCode = "404", description = "Tariff not found", content = @Content)
    })
    public TariffDto getTariffWithRevisions(
            @PathVariable String name,
            @RequestParam(value = "revisionNumber", required = false) Long revisionNumber) {

        try {
            if (revisionNumber != null) {
                return tariffMapper.mapTariffToTariffDto(tariffService.getBaseObjectWithRevision(name, revisionNumber));
            }
            else {
                return tariffMapper.mapTariffToTariffDto(tariffService.findByName(name));
            }
        } catch (ObjectNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @PostMapping("")
    @Operation(summary = "Creates new tariff with one revision")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Tariff created",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = TariffDto.class))}
            ),
            @ApiResponse(responseCode = "400", description = "Tariff could not be created", content = @Content)
    })
    public TariffDto newTariff(@RequestBody TariffDto tariffDto) {

        try {
            return tariffMapper.mapTariffToTariffDto(tariffService.newBaseObject(tariffMapper.mapTariffDtoToTariff(tariffDto)));
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

    @PostMapping("/{name}")
    @Operation(summary = "Adds new revision to existing tariff")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Revision added",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = TariffDto.class))}
            ),
            @ApiResponse(responseCode = "404", description = "Tariff not found", content = @Content)
    })
    public TariffDto addRevision(@RequestBody TariffRevisionDto tariffRevisionDto, @PathVariable String name) {

        try {
            return tariffMapper.mapTariffToTariffDto(
                    tariffService.addRevision(name, tariffMapper.mapTariffRevisionDtoToTariffRevision(tariffRevisionDto)));
        } catch (ObjectNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @DeleteMapping("/{name}")
    @Operation(summary = "Deletes tariff with all revisions")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Tariff deleted",
                    content = @Content
            ),
            @ApiResponse(responseCode = "404", description = "Tariff not found", content = @Content)
    })
    public void deleteTariff(@PathVariable String name) {

        try {
            tariffService.deleteBaseObject(name);
        } catch (ObjectNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @DeleteMapping("/{name}/{revisionNumber}")
    @Operation(summary = "Deletes revision of tariff")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Revision deleted",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = TariffDto.class))}
            ),
            @ApiResponse(responseCode = "400", description = "Could not delete last revision of given tariff", content = @Content),
            @ApiResponse(responseCode = "404", description = "Tariff or revision not found", content = @Content)
    })
    public TariffDto deleteTariffRevision(@PathVariable String name, @PathVariable Long revisionNumber) {

        try {
            return tariffMapper.mapTariffToTariffDto(tariffService.deleteBaseObjectRevision(name, revisionNumber));
        } catch (ObjectNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        } catch (IllegalStateException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }
}
