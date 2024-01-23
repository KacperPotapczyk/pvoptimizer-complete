package com.github.kacperpotapczyk.pvoptimizer.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kacperpotapczyk.pvoptimizer.backend.dto.demand.DemandDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.dto.demand.DemandRevisionDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.dto.demand.DemandValueDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.demand.Demand;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.demand.DemandRevision;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.demand.DemandValue;
import com.github.kacperpotapczyk.pvoptimizer.backend.service.DemandService;
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
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class DemandControllerTest {

    // test data in test/resources/data.sql
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final DemandService demandService;
    public static final MediaType APPLICATION_JSON_UTF8 = MediaType.APPLICATION_JSON;

    @Autowired
    public DemandControllerTest(MockMvc mockMvc, ObjectMapper objectMapper, DemandService demandService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.demandService = demandService;
    }

    @Test
    public void getDemandWithAllRevisions() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/demands/{name}", "queryOnly"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("name", is("queryOnly")))
                .andExpect(jsonPath("revisions", hasSize(2)));
    }

    @Test
    public void demandNotExists() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/demands/{name}", "unknown"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getDemandWithSpecificRevision() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/demands/{name}?revisionNumber=1", "queryOnly"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("name", is("queryOnly")))
                .andExpect(jsonPath("revisions", hasSize(1)))
                .andExpect(jsonPath("revisions[0].revisionNumber", is(1)))
                .andExpect(jsonPath("revisions[0].demandValues", hasSize(2)))
                .andExpect(jsonPath("revisions[0].demandValues[*].value", containsInAnyOrder(4.5, 4.1)))
                .andExpect(jsonPath("revisions[0].demandValues[*].dateTime", containsInAnyOrder("2023-10-05T12:00:00", "2023-10-05T13:00:00")));
    }

    @Test
    public void revisionDoesNotExists() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/demands/{name}?revisionNumber=6342", "queryOnly"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void newDemandTest() throws Exception {

        LocalDateTime dateTime = LocalDateTime.parse("2023-10-05T11:00:00");
        double value = 3.3;
        DemandRevisionDto demandRevisionDto = getDemandRevisionDto(dateTime, value);

        Set<DemandRevisionDto> demandRevisionDtoSet = new HashSet<>();
        demandRevisionDtoSet.add(demandRevisionDto);

        String demandName = "PostDemand";
        DemandDto demandDto = new DemandDto(demandName, demandRevisionDtoSet);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/demands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(demandDto)))
                .andExpect(status().isOk());

        Demand demand = demandService.findByName(demandName);
        assertEquals(1, demand.getRevisions().size());

        DemandRevision demandRevision = demand.getRevisions().stream().findFirst().orElseThrow();
        assertEquals(1, demandRevision.getDemandValues().size());

        DemandValue demandValue = demandRevision.getDemandValues().stream().findFirst().orElseThrow();
        assertEquals(dateTime, demandValue.getDateTime());
        assertEquals(value, demandValue.getValue());
    }

    @Test
    @Transactional
    public void addRevisionToDemand() throws Exception {

        String demandName = "addRevisionTest";
        Demand demand = demandService.findByName(demandName);

        int numberOfRevisions = demand.getRevisions().size();

        DemandRevisionDto demandRevisionDto = getDemandRevisionDto(LocalDateTime.parse("2023-10-05T11:00:00"), 3.3);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/demands/{name}", demandName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(demandRevisionDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("name", is(demandName)))
                .andExpect(jsonPath("revisions", hasSize(numberOfRevisions + 1)));
    }

    @Test
    public void addRevisionToNotExistingDemand() throws Exception {

        DemandRevisionDto demandRevisionDto = getDemandRevisionDto(LocalDateTime.parse("2023-10-05T11:00:00"), 3.3);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/demands/{name}", "unknownDemand")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(demandRevisionDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteDemandRevision() throws Exception {

        String demandName = "revisionToBeDeletedHttpRequest";

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/demands/{name}/{revisionNumber}", demandName, 2))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("name", is(demandName)))
                .andExpect(jsonPath("revisions", hasSize(1)));
    }

    @Test
    public void deleteDemand() throws Exception {

        String demandName = "toBeDeletedHttpRequest";

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/demands/{name}", demandName))
                .andExpect(status().isOk());

        assertFalse(demandService.existsByName(demandName));
    }

    private DemandRevisionDto getDemandRevisionDto(LocalDateTime dateTime, double value) {

        DemandValueDto demandValueDto = new DemandValueDto(dateTime, value);
        Set<DemandValueDto> demandValueDtoSet = new HashSet<>();
        demandValueDtoSet.add(demandValueDto);

        return new DemandRevisionDto(1L, demandValueDtoSet);
    }
}
