package com.github.kacperpotapczyk.pvoptimizer.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kacperpotapczyk.pvoptimizer.backend.dto.cyclicalvalue.CyclicalDailyValueDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.dto.cyclicalvalue.DailyTimeValueDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.dto.cyclicalvalue.WeekdaysDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.dto.tariff.TariffDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.dto.tariff.TariffRevisionDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.tariff.Tariff;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.tariff.TariffRevision;
import com.github.kacperpotapczyk.pvoptimizer.backend.service.TariffService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalTime;
import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TariffControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final TariffService tariffService;
    public static final MediaType APPLICATION_JSON_UTF8 = MediaType.APPLICATION_JSON;

    @Autowired
    public TariffControllerTest(MockMvc mockMvc, ObjectMapper objectMapper, TariffService tariffService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.tariffService = tariffService;
    }

    @Test
    public void getTariffWithAllRevisions() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/tariffs/{name}", "queryOnly"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("name", is("queryOnly")))
                .andExpect(jsonPath("revisions", hasSize(2)));
    }

    @Test
    public void tariffNotExists() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/tariffs/{name}", "unknown"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getTariffWithSpecificRevision() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/tariffs/{name}?revisionNumber=1", "queryOnly"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("name", is("queryOnly")))
                .andExpect(jsonPath("revisions", hasSize(1)))
                .andExpect(jsonPath("revisions[0].revisionNumber", is(1)))
                .andExpect(jsonPath("revisions[0].defaultPrice", is(0.02)));
    }

    @Test
    public void tariffRevisionDoesNotExists() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/tariffs/{name}?revisionNumber=6342", "queryOnly"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void newTariffTest() throws Exception {

        String tariffName = "PostTariff";
        double value = 0.234;
        TariffRevisionDto tariffRevisionDto = new TariffRevisionDto(1L, value, Collections.emptyList());

        Set<TariffRevisionDto> tariffRevisionDtoSet = new HashSet<>();
        tariffRevisionDtoSet.add(tariffRevisionDto);

        TariffDto tariffDto = new TariffDto(tariffName, tariffRevisionDtoSet);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/tariffs")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(tariffDto)))
                .andExpect(status().isOk());

        Tariff tariff = tariffService.findByName(tariffName);
        assertEquals(1, tariff.getRevisions().size());

        TariffRevision tariffRevision = tariff.getRevision(1L).orElseThrow();
        assertEquals(value, tariffRevision.getDefaultPrice());
    }

    @Test
    @Transactional
    public void addRevisionToTariff() throws Exception {

        String tariffName = "addRevisionTest";
        Tariff tariff = tariffService.findByName(tariffName);

        int numberOfRevisions = tariff.getRevisions().size();

        double value = 1.234;
        TariffRevisionDto tariffRevisionDto = new TariffRevisionDto(1L, value, getCyclicalDailyValueListDto());

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/tariffs/{name}", tariffName)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(tariffRevisionDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("name", is(tariffName)))
                .andExpect(jsonPath("revisions", hasSize(numberOfRevisions + 1)))
                .andExpect(jsonPath("revisions[0].defaultPrice", is(value)))
                .andExpect(jsonPath("revisions[0].cyclicalDailyValues", hasSize(1)))
                .andExpect(jsonPath("revisions[0].cyclicalDailyValues[0].dayOfTheWeek", is("ALL")))
                .andExpect(jsonPath("revisions[0].cyclicalDailyValues[0].dailyTimeValues", hasSize(2)))
                .andExpect(jsonPath("revisions[0].cyclicalDailyValues[0].dailyTimeValues[0].startTime", is("07:00:00")))
                .andExpect(jsonPath("revisions[0].cyclicalDailyValues[0].dailyTimeValues[0].currentValue", is(0.1)))
                .andExpect(jsonPath("revisions[0].cyclicalDailyValues[0].dailyTimeValues[1].startTime", is("09:00:00")))
                .andExpect(jsonPath("revisions[0].cyclicalDailyValues[0].dailyTimeValues[1].currentValue", is(0.15)));
    }

    private List<CyclicalDailyValueDto> getCyclicalDailyValueListDto() {

        DailyTimeValueDto value1 = new DailyTimeValueDto(LocalTime.parse("07:00:00"), 0.1);
        DailyTimeValueDto value2 = new DailyTimeValueDto(LocalTime.parse("09:00:00"), 0.15);

        List<DailyTimeValueDto> dailyTimeValueListDtoList = new ArrayList<>();
        dailyTimeValueListDtoList.add(value1);
        dailyTimeValueListDtoList.add(value2);
        CyclicalDailyValueDto cyclicalDailyValue = new CyclicalDailyValueDto(WeekdaysDto.ALL, dailyTimeValueListDtoList);

        List<CyclicalDailyValueDto> CyclicalDailyValueDtoList = new ArrayList<>();
        CyclicalDailyValueDtoList.add(cyclicalDailyValue);
        return CyclicalDailyValueDtoList;
    }

    @Test
    public void addRevisionToNotExistingTariff() throws Exception {

        double value = 1.234;
        TariffRevisionDto tariffRevisionDto = new TariffRevisionDto(1L, value, Collections.emptyList());

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/tariffs/{name}", "unknownDemand")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(tariffRevisionDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteTariffRevision() throws Exception {

        String tariffName = "revisionToBeDeletedHttpRequest";

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/tariffs/{name}/{revisionNumber}", tariffName, 2))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("name", is(tariffName)))
                .andExpect(jsonPath("revisions", hasSize(1)));
    }

    @Test
    public void deleteTariff() throws Exception {

        String tariffName = "toBeDeletedHttpRequest";

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/tariffs/{name}", tariffName))
                .andExpect(status().isOk());

        assertFalse(tariffService.existsByName(tariffName));
    }
}
