package com.github.kacperpotapczyk.pvoptimizer.backend.controller;

import com.github.kacperpotapczyk.pvoptimizer.backend.dto.contract.ContractDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.dto.contract.ContractRevisionDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.dto.contract.ContractRevisionsListDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.contract.Contract;
import com.github.kacperpotapczyk.pvoptimizer.backend.mapper.ContractMapper;
import com.github.kacperpotapczyk.pvoptimizer.backend.service.ContractService;
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
@RequestMapping("/api/contracts")
public class ContractController {

    private final ContractService contractService;
    private final ContractMapper contractMapper;

    @Autowired
    public ContractController(ContractService contractService, ContractMapper contractMapper) {
        this.contractService = contractService;
        this.contractMapper = contractMapper;
    }

    @GetMapping("/list")
    @Operation(summary = "Returns list of all Contracts with their revisions")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ContractRevisionsListDto.class))})
    })
    public Page<ContractRevisionsListDto> getContractListWithRevisions(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "25") int pageSize
    ) {

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("id").ascending());
        Page<Contract> contracts = contractService.getBaseObjects(pageable);
        return contracts.map(contractMapper::mapContractToContractRevisionsListDto);
    }

    @GetMapping("/{name}")
    @Operation(summary = "Returns Contract by its name with all revisions. If revision number is present returns Contract with given revision")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Contract found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ContractDto.class))}
            ),
            @ApiResponse(responseCode = "404", description = "Contract not found", content = @Content)
    })
    public ContractDto getContractWithRevisions(
            @PathVariable String name,
            @RequestParam(value = "revisionNumber", required = false) Long revisionNumber
    ) {
        try {
            if (revisionNumber != null) {
                return contractMapper.mapContractToContractDto(contractService.getBaseObjectWithRevision(name, revisionNumber));
            }
            else {
                return contractMapper.mapContractToContractDto(contractService.findByName(name));
            }
        }
        catch (ObjectNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @PostMapping("")
    @Operation(summary = "Creates new contract with one revision")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Contract created",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ContractDto.class))}
            ),
            @ApiResponse(responseCode = "400", description = "Contract could not be created", content = @Content)
    })
    public ContractDto newContract(@RequestBody ContractDto contractDto) {

        try {
            return contractMapper.mapContractToContractDto(contractService.newBaseObject(contractMapper.mapContractDtoToContract(contractDto)));
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

    @PostMapping("/{name}")
    @Operation(summary = "Adds new revision to existing contract")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Revision added",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ContractDto.class))}
            ),
            @ApiResponse(responseCode = "400", description = "Illegal revision values", content = @Content),
            @ApiResponse(responseCode = "404", description = "Contract not found", content = @Content)
    })
    public ContractDto addRevision(@RequestBody ContractRevisionDto contractRevisionDto, @PathVariable String name) {

        try {
            return contractMapper.mapContractToContractDto(
                    contractService.addRevision(name, contractMapper.mapContractRevisionDtoToContractRevision(contractRevisionDto)));
        } catch (ObjectNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

    @DeleteMapping("/{name}")
    @Operation(summary = "Deletes Contract with all revisions")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Contract deleted",
                    content = @Content
            ),
            @ApiResponse(responseCode = "404", description = "Contract not found", content = @Content)
    })
    public void deleteContract(@PathVariable String name) {

        try {
            contractService.deleteBaseObject(name);
        } catch (ObjectNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @DeleteMapping("/{name}/{revisionNumber}")
    @Operation(summary = "Deletes revision of contract")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Revision deleted",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ContractDto.class))}
            ),
            @ApiResponse(responseCode = "400", description = "Could not delete last revision of given tariff", content = @Content),
            @ApiResponse(responseCode = "404", description = "Contract or revision not found", content = @Content)
    })
    public ContractDto deleteContractRevision(@PathVariable String name, @PathVariable Long revisionNumber) {

        try {
            return contractMapper.mapContractToContractDto(contractService.deleteBaseObjectRevision(name, revisionNumber));
        } catch (ObjectNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        } catch (IllegalStateException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }
}
