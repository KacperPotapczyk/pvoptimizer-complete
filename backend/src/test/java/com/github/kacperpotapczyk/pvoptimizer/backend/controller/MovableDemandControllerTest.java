package com.github.kacperpotapczyk.pvoptimizer.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kacperpotapczyk.pvoptimizer.backend.dto.movabledemand.MovableDemandDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.dto.movabledemand.MovableDemandRevisionDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.dto.movabledemand.MovableDemandStartDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.dto.movabledemand.MovableDemandValueDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.movabledemand.MovableDemand;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.movabledemand.MovableDemandRevision;
import com.github.kacperpotapczyk.pvoptimizer.backend.service.MovableDemandService;
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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class MovableDemandControllerTest {
    // test data in test/resources/data.sql
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final MovableDemandService movableDemandService;
    public static final MediaType APPLICATION_JSON_UTF8 = MediaType.APPLICATION_JSON;

    @Autowired
    public MovableDemandControllerTest(MockMvc mockMvc, ObjectMapper objectMapper, MovableDemandService movableDemandService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.movableDemandService = movableDemandService;
    }

    @Test
    public void getMovableDemandWithAllRevisions() throws Exception {

        String baseName = "queryOnly";
        mockMvc.perform(MockMvcRequestBuilders.get("/api/movable-demands/{name}", baseName))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("name", is(baseName)))
                .andExpect(jsonPath("revisions", hasSize(2)));
    }

    @Test
    public void getMovableDemandWithSpecificRevision() throws Exception {

        String baseName = "queryOnly";
        mockMvc.perform(MockMvcRequestBuilders.get("/api/movable-demands/{name}?revisionNumber=1", baseName))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("name", is(baseName)))
                .andExpect(jsonPath("revisions", hasSize(1)))
                .andExpect(jsonPath("revisions[0].revisionNumber", is(1)))
                .andExpect(jsonPath("revisions[0].movableDemandStarts", hasSize(2)))
                .andExpect(jsonPath("revisions[0].movableDemandStarts[0].start", is("2023-01-01T10:00:00")))
                .andExpect(jsonPath("revisions[0].movableDemandStarts[1].start", is("2023-01-01T10:15:00")))
                .andExpect(jsonPath("revisions[0].movableDemandValues", hasSize(2)))
                .andExpect(jsonPath("revisions[0].movableDemandValues[0].order", is(1)))
                .andExpect(jsonPath("revisions[0].movableDemandValues[0].durationMinutes", is(10)))
                .andExpect(jsonPath("revisions[0].movableDemandValues[0].value", is(5.0)))
                .andExpect(jsonPath("revisions[0].movableDemandValues[1].order", is(2)))
                .andExpect(jsonPath("revisions[0].movableDemandValues[1].durationMinutes", is(5)))
                .andExpect(jsonPath("revisions[0].movableDemandValues[1].value", is(3.0)));
    }

    @Test
    public void revisionDesNotExists() throws Exception {

        String baseName = "queryOnly";
        mockMvc.perform(MockMvcRequestBuilders.get("/api/movable-demands/{name}?revisionNumber=45623", baseName))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void newMovableDemandTest() throws Exception {

        String baseName = "PostMovableDemand";
        LocalDateTime dateTime = LocalDateTime.parse("2023-10-05T11:00:00");
        long order = 1L;
        double value = 23;
        long durationMinutes = 4L;

        Set<MovableDemandRevisionDto> movableDemandRevisionDtoSet = new HashSet<>();
        movableDemandRevisionDtoSet.add(getMovableDemandRevisionDto(dateTime, order, durationMinutes, value));

        MovableDemandDto movableDemandDto = new MovableDemandDto(baseName, movableDemandRevisionDtoSet);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/movable-demands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movableDemandDto)))
                .andExpect(status().isOk());

        MovableDemand movableDemand = movableDemandService.findByName(baseName);
        assertEquals(1, movableDemand.getRevisions().size());

        MovableDemandRevision movableDemandRevision = movableDemand.getRevision(1L).orElseThrow();
        assertEquals(1, movableDemandRevision.getMovableDemandStarts().size());
        assertEquals(1, movableDemandRevision.getMovableDemandValues().size());
        assertEquals(value, movableDemandRevision.getMovableDemandValues().get(0).getValue());
        assertEquals(durationMinutes, movableDemandRevision.getMovableDemandValues().get(0).getDurationMinutes());
        assertEquals(order, movableDemandRevision.getMovableDemandValues().get(0).getOrder());
    }

    @Test
    @Transactional
    public void addRevisionToMovableDemand() throws Exception {

        String baseName = "addRevisionTest";
        LocalDateTime dateTime = LocalDateTime.parse("2023-10-05T12:00:00");
        long order = 1L;
        double value = 13;
        long durationMinutes = 14L;

        MovableDemand movableDemand = movableDemandService.findByName(baseName);
        int expectedNumberOfRevisions = movableDemand.getRevisions().size() + 1;

        MovableDemandRevisionDto movableDemandRevisionDto = getMovableDemandRevisionDto(dateTime, order, durationMinutes, value);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/movable-demands/{name}", baseName)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(movableDemandRevisionDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is(baseName)))
                .andExpect(jsonPath("revisions", hasSize(expectedNumberOfRevisions)));
    }

    @Test
    public void addRevisionToNotExistingMovableDemand() throws Exception {

        LocalDateTime dateTime = LocalDateTime.parse("2023-10-05T12:00:00");
        long order = 1L;
        double value = 13;
        long durationMinutes = 14L;

        MovableDemandRevisionDto movableDemandRevisionDto = getMovableDemandRevisionDto(dateTime, order, durationMinutes, value);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/movable-demands/{name}", "unknown name")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(movableDemandRevisionDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteMovableDemandRevision() throws Exception {

        String baseName = "revisionToBeDeletedHttpRequest";

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/movable-demands/{name}/{revisionNumber}", baseName, 2))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("name", is(baseName)))
                .andExpect(jsonPath("revisions", hasSize(1)));
    }

    @Test
    public void deleteMovableDemand() throws Exception {

        String baseName = "toBeDeletedHttpRequest";

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/api/movable-demands/{name}", baseName))
                .andExpect(status().isOk());
        assertFalse(movableDemandService.existsByName(baseName));
    }

    private MovableDemandRevisionDto getMovableDemandRevisionDto(LocalDateTime dateTime, long order, long durationMinutes, double value) {

        Set<MovableDemandStartDto> movableDemandStartDtoSet = new HashSet<>();
        movableDemandStartDtoSet.add(new MovableDemandStartDto(dateTime));

        List<MovableDemandValueDto> movableDemandValueDtoList = new ArrayList<>();
        movableDemandValueDtoList.add(new MovableDemandValueDto(order, durationMinutes, value));

        return new MovableDemandRevisionDto(
                1,
                movableDemandStartDtoSet,
                movableDemandValueDtoList
        );
    }
}
