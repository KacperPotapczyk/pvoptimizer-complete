package com.github.kacperpotapczyk.pvoptimizer.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kacperpotapczyk.pvoptimizer.backend.dto.production.ProductionDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.dto.production.ProductionRevisionDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.dto.production.ProductionValueDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.production.Production;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.production.ProductionRevision;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.production.ProductionValue;
import com.github.kacperpotapczyk.pvoptimizer.backend.service.ProductionService;
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
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProductionControllerTest {

    // test data in test/resources/data.sql
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final ProductionService productionService;
    public static final MediaType APPLICATION_JSON_UTF8 = MediaType.APPLICATION_JSON;

    @Autowired
    ProductionControllerTest(MockMvc mockMvc, ObjectMapper objectMapper, ProductionService productionService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.productionService = productionService;
    }

    @Test
    public void getProductionWithAllRevisions() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/productions/{name}", "queryOnly"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("name", is("queryOnly")))
                .andExpect(jsonPath("revisions", hasSize(2)));
    }

    @Test
    public void getProductionWithSpecificRevision() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/productions/{name}?revisionNumber=1", "queryOnly"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("name", is("queryOnly")))
                .andExpect(jsonPath("revisions", hasSize(1)))
                .andExpect(jsonPath("revisions[0].revisionNumber", is(1)))
                .andExpect(jsonPath("revisions[0].productionValues", hasSize(2)))
                .andExpect(jsonPath("revisions[0].productionValues[*].value", containsInAnyOrder(4.5, 4.1)))
                .andExpect(jsonPath("revisions[0].productionValues[*].dateTime", containsInAnyOrder("2023-10-05T12:00:00", "2023-10-05T13:00:00")));
    }

    @Test
    public void revisionDoesNotExists() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/productions/{name}?revisionNumber=6342", "queryOnly"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void newProductionTest() throws Exception {

        LocalDateTime dateTime = LocalDateTime.parse("2023-10-05T11:00:00");
        double value = 3.3;
        ProductionRevisionDto productionRevisionDto = getProductionRevisionDto(dateTime, value);

        Set<ProductionRevisionDto> productionRevisionDtoSet = new HashSet<>();
        productionRevisionDtoSet.add(productionRevisionDto);

        String productionName = "PostProduction";
        ProductionDto productionDto = new ProductionDto(productionName, productionRevisionDtoSet);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/productions")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(productionDto)))
                .andExpect(status().isOk());

        Production production = productionService.findByName(productionName);
        assertEquals(1, production.getRevisions().size());

        ProductionRevision productionRevision = production.getRevisions().stream().findFirst().orElseThrow();
        assertEquals(1, productionRevision.getProductionValues().size());

        ProductionValue productionValue = productionRevision.getProductionValues().stream().findFirst().orElseThrow();
        assertEquals(dateTime, productionValue.getDateTime());
        assertEquals(value, productionValue.getValue());
    }

    @Test
    @Transactional
    public void addRevisionToProduction() throws Exception {

        String productionName = "addRevisionTest";
        Production production = productionService.findByName(productionName);

        int numberOfRevisions = production.getRevisions().size();

        ProductionRevisionDto productionRevisionDto = getProductionRevisionDto(LocalDateTime.parse("2023-10-05T11:00:00"), 3.3);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/productions/{name}", productionName)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(productionRevisionDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("name", is(productionName)))
                .andExpect(jsonPath("revisions", hasSize(numberOfRevisions + 1)));
    }

    @Test
    public void addRevisionToNotExistingProduction() throws Exception {

        ProductionRevisionDto productionRevisionDto = getProductionRevisionDto(LocalDateTime.parse("2023-10-05T11:00:00"), 3.3);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/productions/{name}", "unknownDemand")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(productionRevisionDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteProductionRevision() throws Exception {

        String productionName = "revisionToBeDeletedHttpRequest";

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/api/productions/{name}/{revisionNumber}", productionName, 2))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("name", is(productionName)))
                .andExpect(jsonPath("revisions", hasSize(1)));
    }

    @Test
    public void deleteProduction() throws Exception {

        String productionName = "toBeDeletedHttpRequest";

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/api/productions/{name}", productionName))
                .andExpect(status().isOk());

        assertFalse(productionService.existsByName(productionName));
    }

    private ProductionRevisionDto getProductionRevisionDto(LocalDateTime dateTime, double value) {

        ProductionValueDto productionValueDto = new ProductionValueDto(dateTime, value);
        Set<ProductionValueDto> productionValueDtoSet = new HashSet<>();
        productionValueDtoSet.add(productionValueDto);

        return new ProductionRevisionDto(1L, productionValueDtoSet);
    }
}