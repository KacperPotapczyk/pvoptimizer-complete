package com.github.kacperpotapczyk.pvoptimizer.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kacperpotapczyk.pvoptimizer.backend.dto.contract.*;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.contract.Contract;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.contract.ContractRevision;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.contract.ContractType;
import com.github.kacperpotapczyk.pvoptimizer.backend.service.ContractService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ContractControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final ContractService contractService;
    public static final MediaType APPLICATION_JSON_UTF8 = MediaType.APPLICATION_JSON;

    @Autowired
    public ContractControllerTest(MockMvc mockMvc, ObjectMapper objectMapper, ContractService contractService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.contractService = contractService;
    }

    @Test
    public void getContractWithAllRevisions() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/contracts/{name}", "queryOnly"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("name", is("queryOnly")))
                .andExpect(jsonPath("revisions", hasSize(2)));
    }

    @Test
    public void contractNotExists() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/contracts/{name}", "unknown"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getContractWithSpecificRevision() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/contracts/{name}?revisionNumber=1", "queryOnly"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("name", is("queryOnly")))
                .andExpect(jsonPath("revisions", hasSize(1)))
                .andExpect(jsonPath("contractType", is("PURCHASE")))
                .andExpect(jsonPath("revisions[0].revisionNumber", is(1)))
                .andExpect(jsonPath("revisions[0].contractMinEnergyConstraints", hasSize(2)))
                .andExpect(jsonPath("revisions[0].contractMaxEnergyConstraints", hasSize(0)))
                .andExpect(jsonPath("revisions[0].contractMinPowerConstraints", hasSize(0)))
                .andExpect(jsonPath("revisions[0].contractMaxPowerConstraints", hasSize(0)));
    }

    @Test
    public void contractRevisionDoesNotExists() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/contracts/{name}?revisionNumber=6342", "queryOnly"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void newContractTest() throws Exception {

        String contractName = "PostContract";
        ContractTypeDto contractTypeDto = ContractTypeDto.SELL;

        LocalDateTime startDate = LocalDateTime.parse("2023-10-05T12:00:00");
        LocalDateTime endDate = LocalDateTime.parse("2023-11-05T12:00:00");
        double minPowerValue = 3.1;

        ContractDto contractDto = getContractDto(startDate, endDate, minPowerValue, contractName, contractTypeDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/contracts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(contractDto)))
                .andExpect(status().isOk());

        Contract contract = contractService.findByName(contractName);
        assertEquals(1, contract.getRevisions().size());
        assertEquals(ContractType.SELL, contract.getContractType());

        ContractRevision contractRevision = contract.getRevision(1L).orElseThrow();
        assertEquals(1, contractRevision.getContractMinPowerConstraints().size());
        assertEquals(0, contractRevision.getContractMaxPowerConstraints().size());
        assertEquals(0, contractRevision.getContractMinEnergyConstraints().size());
        assertEquals(0, contractRevision.getContractMaxEnergyConstraints().size());
    }

    @Test
    @Transactional
    public void addRevisionToContract() throws Exception {

        String contractName = "addRevisionTest";
        LocalDateTime startDate = LocalDateTime.parse("2023-10-05T12:00:00");
        LocalDateTime endDate = LocalDateTime.parse("2023-11-05T12:00:00");
        double minPowerValue = 1.1;

        Contract contract = contractService.findByName(contractName);
        int numberOfRevisions = contract.getRevisions().size();

        ContractRevisionDto contractRevisionDto = getContractRevisionDto(startDate, endDate, minPowerValue);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/contracts/{name}", contractName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contractRevisionDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("name", is(contractName)))
                .andExpect(jsonPath("revisions", hasSize(numberOfRevisions + 1)));
    }

    @Test
    public void addRevisionToNotExistingContract() throws Exception {

        LocalDateTime startDate = LocalDateTime.parse("2023-11-05T11:00:00");
        LocalDateTime endDate = LocalDateTime.parse("2023-11-05T12:00:00");
        double minPowerValue = 1.1;

        ContractRevisionDto contractRevisionDto = getContractRevisionDto(startDate, endDate, minPowerValue);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/contracts/{name}", "unknownDemand")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contractRevisionDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteTariffRevision() throws Exception {

        String contractName = "revisionToBeDeletedHttpRequest";

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/contracts/{name}/{revisionNumber}", contractName, 2))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("name", is(contractName)))
                .andExpect(jsonPath("revisions", hasSize(1)));
    }

    @Test
    public void deleteContract() throws Exception {

        String contractName = "toBeDeletedHttpRequest";

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/contracts/{name}", contractName))
                .andExpect(status().isOk());

        assertFalse(contractService.existsByName(contractName));
    }

    private ContractDto getContractDto(LocalDateTime startDate, LocalDateTime endDate, double minPowerValue, String contractName, ContractTypeDto contractTypeDto) {

        ContractRevisionDto contractRevisionDto = getContractRevisionDto(startDate, endDate, minPowerValue);

        Set<ContractRevisionDto> contractRevisionDtoSet = new HashSet<>();
        contractRevisionDtoSet.add(contractRevisionDto);

        return new ContractDto(contractName, contractTypeDto, new ContractTariffDto("queryOnly"), contractRevisionDtoSet);
    }

    private ContractRevisionDto getContractRevisionDto(LocalDateTime startDate, LocalDateTime endDate, double minPowerValue) {
        ContractConstraintDto contractConstraintDto = new ContractConstraintDto(startDate, endDate, minPowerValue);

        List<ContractConstraintDto> contractConstraintDtoList = new ArrayList<>();
        contractConstraintDtoList.add(contractConstraintDto);

        List<ContractConstraintDto> emptyList = new ArrayList<>();

        return new ContractRevisionDto(
                1,
                contractConstraintDtoList,
                emptyList,
                emptyList,
                emptyList
        );
    }
}
