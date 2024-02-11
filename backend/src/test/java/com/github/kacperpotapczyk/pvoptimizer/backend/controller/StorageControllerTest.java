package com.github.kacperpotapczyk.pvoptimizer.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kacperpotapczyk.pvoptimizer.backend.dto.storage.StorageConstraintDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.dto.storage.StorageDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.dto.storage.StorageRevisionDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.storage.Storage;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.storage.StorageMinEnergyConstraint;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.storage.StorageRevision;
import com.github.kacperpotapczyk.pvoptimizer.backend.service.StorageService;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class StorageControllerTest {

    // test data in test/resources/data.sql
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final StorageService storageService;
    public static final MediaType APPLICATION_JSON_UTF8 = MediaType.APPLICATION_JSON;

    @Autowired
    StorageControllerTest(MockMvc mockMvc, ObjectMapper objectMapper, StorageService storageService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.storageService = storageService;
    }

    @Test
    public void getStorageWithAllRevisions() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/storages/{name}", "queryOnly"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("name", is("queryOnly")))
                .andExpect(jsonPath("capacity", is(100.0)))
                .andExpect(jsonPath("maxCharge", is(10.0)))
                .andExpect(jsonPath("maxDischarge", is(20.0)))
                .andExpect(jsonPath("revisions", hasSize(2)));
    }

    @Test
    public void getStorageWithSpecificRevision() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/storages/{name}?revisionNumber=1", "queryOnly"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("name", is("queryOnly")))
                .andExpect(jsonPath("capacity", is(100.0)))
                .andExpect(jsonPath("maxCharge", is(10.0)))
                .andExpect(jsonPath("maxDischarge", is(20.0)))
                .andExpect(jsonPath("revisions", hasSize(1)))
                .andExpect(jsonPath("revisions[0].revisionNumber", is(1)))
                .andExpect(jsonPath("revisions[0].initialEnergy", is(40.0)))
                .andExpect(jsonPath("revisions[0].storageMinChargeConstraints", hasSize(1)))
                .andExpect(jsonPath("revisions[0].storageMinChargeConstraints[0].constraintValue", is(2.0)))
                .andExpect(jsonPath("revisions[0].storageMinChargeConstraints[0].dateTimeStart", is("2024-02-09T17:00:00")))
                .andExpect(jsonPath("revisions[0].storageMinChargeConstraints[0].dateTimeEnd", is("2024-02-09T17:15:00")))
                .andExpect(jsonPath("revisions[0].storageMaxEnergyConstraints", hasSize(1)))
                .andExpect(jsonPath("revisions[0].storageMaxEnergyConstraints[0].constraintValue", is(95.0)))
                .andExpect(jsonPath("revisions[0].storageMaxEnergyConstraints[0].dateTimeStart", is("2024-02-09T17:00:00")))
                .andExpect(jsonPath("revisions[0].storageMaxEnergyConstraints[0].dateTimeEnd", is("2024-02-09T17:15:00")));
    }

    @Test
    public void revisionDoesNotExists() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/storages/{name}?revisionNumber=6342", "queryOnly"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void newStorageTest() throws Exception {

        String baseName = "base POST";
        double capacity = 50;
        double maxCharge = 5;
        double maxDischarge = 20;
        double initialEnergy = 30;
        double minEnergyValue = 1;
        LocalDateTime dateTimeStart = LocalDateTime.parse("2023-10-05T11:00:00");
        LocalDateTime dateTimeEnd = LocalDateTime.parse("2023-10-05T11:15:00");

        StorageDto storageDto = getStorageDto(
                baseName,
                capacity,
                maxCharge,
                maxDischarge,
                initialEnergy,
                dateTimeStart,
                dateTimeEnd,
                minEnergyValue
        );

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/storages")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(storageDto)))
                .andExpect(status().isOk());

        Storage storage = storageService.findByName(baseName);
        assertEquals(1, storage.getRevisions().size());
        assertEquals(capacity, storage.getCapacity());
        assertEquals(maxCharge, storage.getMaxCharge());
        assertEquals(maxDischarge, storage.getMaxDischarge());

        StorageRevision storageRevision = storage.getRevisions().stream().findFirst().orElseThrow();
        assertEquals(initialEnergy, storageRevision.getInitialEnergy());
        assertEquals(0, storageRevision.getStorageMinChargeConstraints().size());
        assertEquals(0, storageRevision.getStorageMaxChargeConstraints().size());
        assertEquals(0, storageRevision.getStorageMinDischargeConstraints().size());
        assertEquals(0, storageRevision.getStorageMaxDischargeConstraints().size());
        assertEquals(1, storageRevision.getStorageMinEnergyConstraints().size());
        assertEquals(0, storageRevision.getStorageMaxEnergyConstraints().size());

        StorageMinEnergyConstraint storageMinEnergyConstraint = storageRevision.getStorageMinEnergyConstraints().get(0);
        assertEquals(minEnergyValue, storageMinEnergyConstraint.getConstraintValue());
        assertEquals(dateTimeStart, storageMinEnergyConstraint.getDateTimeStart());
        assertEquals(dateTimeEnd, storageMinEnergyConstraint.getDateTimeEnd());
    }

    @Test
    @Transactional
    public void addRevisionToStorage() throws Exception {

        String baseName = "addRevisionTest";
        LocalDateTime startDate = LocalDateTime.parse("2023-10-05T12:00:00");
        LocalDateTime endDate = LocalDateTime.parse("2023-11-05T12:00:00");
        double minEnergyValue = 1.1;
        double initialEnergy = 20.0;

        Storage storage = storageService.findByName(baseName);
        int expectedNumberOfNewRevision = storage.getRevisions().size() + 1;

        StorageRevisionDto storageRevisionDto = getStorageRevisionDto(initialEnergy, startDate, endDate, minEnergyValue);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/storages/{name}", baseName)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(storageRevisionDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("name", is(baseName)))
                .andExpect(jsonPath("capacity", is(100.0)))
                .andExpect(jsonPath("maxCharge", is(10.0)))
                .andExpect(jsonPath("maxDischarge", is(20.0)))
                .andExpect(jsonPath("revisions", hasSize(expectedNumberOfNewRevision)));
    }

    @Test
    public void addRevisionToNotExistingStorage() throws Exception {

        LocalDateTime startDate = LocalDateTime.parse("2023-10-05T12:00:00");
        LocalDateTime endDate = LocalDateTime.parse("2023-11-05T12:00:00");
        double minEnergyValue = 1.1;
        double initialEnergy = 20.0;

        StorageRevisionDto storageRevisionDto = getStorageRevisionDto(initialEnergy, startDate, endDate, minEnergyValue);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/storages/{name}", "unknownStorage")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(storageRevisionDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteStorageRevision() throws Exception {

        String storageName = "revisionToBeDeletedHttpRequest";

        mockMvc.perform(
                    MockMvcRequestBuilders.delete("/api/storages/{name}/{revisionNumber}", storageName, 2))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("name", is(storageName)))
                .andExpect(jsonPath("revisions", hasSize(1)));
    }

    @Test
    public void deleteStorage() throws Exception {

        String storageName = "toBeDeletedHttpRequest";

        mockMvc.perform(
                    MockMvcRequestBuilders.delete("/api/storages/{name}", storageName))
                .andExpect(status().isOk());

        assertFalse(storageService.existsByName(storageName));
    }

    private StorageDto getStorageDto(
            String storageName,
            double capacity,
            double maxCharge,
            double maxDischarge,
            double initialEnergy,
            LocalDateTime startDate,
            LocalDateTime endDate,
            double minEnergyValue) {

        StorageRevisionDto storageRevisionDto = getStorageRevisionDto(initialEnergy, startDate, endDate, minEnergyValue);

        Set<StorageRevisionDto> storageRevisionDtoSet = new HashSet<>();
        storageRevisionDtoSet.add(storageRevisionDto);

        return new StorageDto(storageName, capacity, maxCharge, maxDischarge, storageRevisionDtoSet);
    }

    private StorageRevisionDto getStorageRevisionDto(double initialEnergy, LocalDateTime startDate, LocalDateTime endDate, double minEnergyValue) {
        StorageConstraintDto storageMinEnergyConstraintDto = new StorageConstraintDto(startDate, endDate, minEnergyValue);

        List<StorageConstraintDto> contractConstraintDtoList = new ArrayList<>();
        contractConstraintDtoList.add(storageMinEnergyConstraintDto);

        List<StorageConstraintDto> emptyList = new ArrayList<>();

        return new StorageRevisionDto(
                1,
                initialEnergy,
                emptyList,
                emptyList,
                emptyList,
                emptyList,
                contractConstraintDtoList,
                emptyList
        );
    }
}