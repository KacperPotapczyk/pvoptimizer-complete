package com.github.kacperpotapczyk.pvoptimizer.backend.controller;

import com.github.kacperpotapczyk.pvoptimizer.backend.dto.production.ProductionDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.dto.production.ProductionRevisionDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.dto.production.ProductionRevisionsListDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.production.Production;
import com.github.kacperpotapczyk.pvoptimizer.backend.mapper.ProductionMapper;
import com.github.kacperpotapczyk.pvoptimizer.backend.service.ProductionService;
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
@RequestMapping("/api/productions")
public class ProductionController {

    private final ProductionService productionService;
    private final ProductionMapper productionMapper;

    @Autowired
    public ProductionController(ProductionService productionService, ProductionMapper productionMapper) {
        this.productionService = productionService;
        this.productionMapper = productionMapper;
    }

    @GetMapping("/list")
    @Operation(summary = "Returns list of all Productions with their revisions")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProductionRevisionsListDto.class))})
    })
    public Page<ProductionRevisionsListDto> getProductionListWithRevisions(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "25") int pageSize) {

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("id").ascending());
        Page<Production> productions = productionService.getBaseObjects(pageable);
        return productions.map(productionMapper::mapProductionToProductionRevisionsListDto);
    }

    @GetMapping("/{name}")
    @Operation(summary = "Returns Production by its name")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Production found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProductionDto.class))}
            ),
            @ApiResponse(responseCode = "404", description = "Production not found", content = @Content)
    })
    public ProductionDto getProductionWithAllRevisions(
            @PathVariable String name,
            @RequestParam(value = "revisionNumber", required = false) Long revisionNumber) {

        try {
            if (revisionNumber != null) {
                return productionMapper.mapProductionToProductionDto(productionService.getBaseObjectWithRevision(name, revisionNumber));
            }
            else {
                return productionMapper.mapProductionToProductionDto(productionService.findByName(name));
            }
        } catch (ObjectNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @PostMapping("")
    @Operation(summary = "Creates new production with one revision")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Production created",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProductionDto.class))}
            ),
            @ApiResponse(responseCode = "400", description = "Production could not be created", content = @Content)
    })
    public ProductionDto newProduction(@RequestBody ProductionDto productionDto) {

        try {
            return productionMapper.mapProductionToProductionDto(productionService.newBaseObject(productionMapper.mapProductionDtoToProduction(productionDto)));
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

    @PostMapping("/{name}")
    @Operation(summary = "Adds new revision to existing production")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Revision added",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProductionDto.class))}
            ),
            @ApiResponse(responseCode = "404", description = "Production not found", content = @Content)
    })
    public ProductionDto addRevision(@RequestBody ProductionRevisionDto productionRevisionDto, @PathVariable String name) {

        try {
            return productionMapper.mapProductionToProductionDto(
                    productionService.addRevision(name, productionMapper.mapProductionRevisionDtoToProductionRevision(productionRevisionDto))
            );
        } catch (ObjectNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @DeleteMapping("/{name}")
    @Operation(summary = "Deletes production with all revisions")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Production deleted",
                    content = @Content
            ),
            @ApiResponse(responseCode = "404", description = "Production not found", content = @Content)
    })
    public void deleteProduction(@PathVariable String name) {

        try {
            productionService.deleteBaseObject(name);
        } catch (ObjectNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @DeleteMapping("/{name}/{revisionNumber}")
    @Operation(summary = "Deletes revision of production")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Revision deleted",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProductionDto.class))}
            ),
            @ApiResponse(responseCode = "400", description = "Could not delete last revision of given production", content = @Content),
            @ApiResponse(responseCode = "404", description = "Production or revision not found", content = @Content)
    })
    public ProductionDto deleteProductionRevision(@PathVariable String name, @PathVariable Long revisionNumber) {

        try {
            return productionMapper.mapProductionToProductionDto(productionService.deleteBaseObjectRevision(name, revisionNumber));
        } catch (ObjectNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        } catch (IllegalStateException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }
}
