package com.github.kacperpotapczyk.pvoptimizer.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kacperpotapczyk.pvoptimizer.backend.dto.result.ResultStatusDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.dto.result.ValidationMessageLevelDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.dto.result.ValidationMessageObjectTypeDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.dto.task.TaskBaseObjectRevisionDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.dto.task.TaskDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.task.Task;
import com.github.kacperpotapczyk.pvoptimizer.backend.service.TaskService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@EmbeddedKafka
@ActiveProfiles("test")
public class TaskControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final TaskService taskService;
    public static final MediaType APPLICATION_JSON_UTF8 = MediaType.APPLICATION_JSON;

    @Autowired
    public TaskControllerTest(MockMvc mockMvc, ObjectMapper objectMapper, TaskService taskService) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.taskService = taskService;
    }

    @Test
    public void getTask() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/tasks/{name}", "queryOnly"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("name", is("queryOnly")))
                .andExpect(jsonPath("demandRevisions", hasSize(1)))
                .andExpect(jsonPath("demandRevisions[0].baseName", is("queryOnly")))
                .andExpect(jsonPath("demandRevisions[0].revisionNumber", is(1)))
                .andExpect(jsonPath("productionRevisions", hasSize(1)))
                .andExpect(jsonPath("productionRevisions[0].baseName", is("queryOnly")))
                .andExpect(jsonPath("productionRevisions[0].revisionNumber", is(1)))
                .andExpect(jsonPath("tariffRevisions", hasSize(1)))
                .andExpect(jsonPath("contractRevisions", hasSize(2)))
                .andExpect(jsonPath("dateTimeStart", is("2023-12-24T14:00:00")))
                .andExpect(jsonPath("dateTimeEnd", is("2023-12-24T17:00:00")));
    }

    @Test
    public void taskDoesNotExists() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/tasks/name", "unknown"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void addTask() throws Exception {

        String taskName = "Post task";
        LocalDateTime dateTimeStart = LocalDateTime.parse("2023-10-05T11:00:00");
        LocalDateTime dateTimeEnd = LocalDateTime.parse("2023-10-05T23:00:00");

        TaskDto taskDto = getTaskDto(taskName, dateTimeStart, dateTimeEnd);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isOk());

        Task task = taskService.findByName(taskName);

        assertEquals(taskName, task.getName());
        assertEquals(dateTimeStart, task.getDateTimeStart());
        assertEquals(dateTimeEnd, task.getDateTimeEnd());
        assertEquals(1, task.getDemandRevisions().size());
        assertEquals(0, task.getTariffRevisions().size());
        assertEquals(1, task.getContractRevisions().size());
        assertEquals(0, task.getStorageRevisions().size());
        assertEquals(0, task.getMovableDemandRevisions().size());
    }

    @Test
    @Transactional
    public void updateTask() throws Exception {

        String taskName = "toBeUpdatedHttpRequest";
        Task task = taskService.findByName(taskName);

        long taskId = task.getId();
        LocalDateTime dateTimeStart = task.getDateTimeStart();
        LocalDateTime dateTimeEnd = LocalDateTime.parse("2023-10-06T23:00:00");

        TaskDto taskDto = getTaskDto(taskName, dateTimeStart, dateTimeEnd);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/api/tasks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isOk());

        Task updatedTask = taskService.findByName(taskName);

        assertEquals(taskId, updatedTask.getId());
        assertEquals(taskName, updatedTask.getName());
        assertEquals(dateTimeStart, updatedTask.getDateTimeStart());
        assertEquals(dateTimeEnd, updatedTask.getDateTimeEnd());
        assertEquals(1, updatedTask.getDemandRevisions().size());
        assertEquals(0, updatedTask.getProductionRevisions().size());
        assertEquals(0, updatedTask.getTariffRevisions().size());
        assertEquals(1, updatedTask.getContractRevisions().size());
        assertEquals(0, task.getStorageRevisions().size());
        assertEquals(0, task.getMovableDemandRevisions().size());
    }

    @Test
    public void deleteTask() throws Exception {

        String taskName = "toBeDeletedHttpRequest";
        assertTrue(taskService.existsByName(taskName));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/tasks/{name}", taskName))
                .andExpect(status().isOk());

        assertFalse(taskService.existsByName(taskName));
    }

    @Test
    public void deleteNotExistingTask() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/tasks/{name}", "unknown"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void sendTaskForCalculation() throws Exception {

        String taskName = "sendToComputeHttpRequest";
        mockMvc.perform(MockMvcRequestBuilders.post("/api/tasks/{name}/calculation", taskName))
                .andExpect(status().isOk());
    }

    @Test
    public void sendNotExistingTaskForCalculation() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/api/tasks/{name}/calculation", "unknown"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getTaskResultValidationOnly() throws Exception {

        String taskName = "readOnly";
        mockMvc.perform(MockMvcRequestBuilders.get("/api/tasks/{name}/result", taskName))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("taskName", is(taskName)))
                .andExpect(jsonPath("resultStatus", is(ResultStatusDto.VALIDATION_SUCCESSFUL.name())))
                .andExpect(jsonPath("objectiveFunctionValue", nullValue()))
                .andExpect(jsonPath("relativeGap", nullValue()))
                .andExpect(jsonPath("elapsedTime", nullValue()))
                .andExpect(jsonPath("optimizerMessage", nullValue()))
                .andExpect(jsonPath("validationMessages", hasSize(1)))
                .andExpect(jsonPath("validationMessages[0].level", is(ValidationMessageLevelDto.INFO.name())))
                .andExpect(jsonPath("validationMessages[0].objectType", is(ValidationMessageObjectTypeDto.CONTRACT.name())))
                .andExpect(jsonPath("validationMessages[0].objectName", is("contract1")))
                .andExpect(jsonPath("validationMessages[0].objectRevision", is(1)))
                .andExpect(jsonPath("validationMessages[0].message", is("test message")))
                .andExpect(jsonPath("contractResults", hasSize(0)));
    }

    @Test
    public void getTaskResultWithContractResult() throws Exception {

        String taskName = "getTaskResultWithContractResult";
        mockMvc.perform(MockMvcRequestBuilders.get("/api/tasks/{name}/result", taskName))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("taskName", is(taskName)))
                .andExpect(jsonPath("resultStatus", is(ResultStatusDto.SOLUTION_FOUND.name())))
                .andExpect(jsonPath("objectiveFunctionValue", is(200.0)))
                .andExpect(jsonPath("relativeGap", is(0.01)))
                .andExpect(jsonPath("elapsedTime", is(12.1)))
                .andExpect(jsonPath("optimizerMessage", is("Optimal solution found")))
                .andExpect(jsonPath("validationMessages", hasSize(0)))
                .andExpect(jsonPath("contractResults", hasSize(1)))
                .andExpect(jsonPath("storageResults", hasSize(0)))
                .andExpect(jsonPath("contractResults[0].contractName", is("queryOnly")))
                .andExpect(jsonPath("contractResults[0].revisionNumber", is(1)))
                .andExpect(jsonPath("contractResults[0].contractResultValues", hasSize(2)))
                .andExpect(jsonPath("contractResults[0].contractResultValues[0].dateTimeStart", is("2023-01-01T10:00:00")))
                .andExpect(jsonPath("contractResults[0].contractResultValues[0].dateTimeEnd", is("2023-01-01T10:15:00")))
                .andExpect(jsonPath("contractResults[0].contractResultValues[0].power", is(10.0)))
                .andExpect(jsonPath("contractResults[0].contractResultValues[0].energy", is(2.5)))
                .andExpect(jsonPath("contractResults[0].contractResultValues[0].cost", is(0.25)))
                .andExpect(jsonPath("contractResults[0].contractResultValues[1].dateTimeStart", is("2023-01-01T10:15:00")))
                .andExpect(jsonPath("contractResults[0].contractResultValues[1].dateTimeEnd", is("2023-01-01T10:30:00")))
                .andExpect(jsonPath("contractResults[0].contractResultValues[1].power", is(20.0)))
                .andExpect(jsonPath("contractResults[0].contractResultValues[1].energy", is(5.0)))
                .andExpect(jsonPath("contractResults[0].contractResultValues[1].cost", is(0.5)));
    }

    @Test
    public void getTaskResultWithStorageResult() throws Exception {

        String taskName = "getTaskResultWithStorageResult";
        mockMvc.perform(MockMvcRequestBuilders.get("/api/tasks/{name}/result", taskName))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("taskName", is(taskName)))
                .andExpect(jsonPath("resultStatus", is(ResultStatusDto.SOLUTION_FOUND.name())))
                .andExpect(jsonPath("objectiveFunctionValue", is(0.0)))
                .andExpect(jsonPath("relativeGap", is(0.01)))
                .andExpect(jsonPath("elapsedTime", is(1.1)))
                .andExpect(jsonPath("optimizerMessage", is("Optimal solution found")))
                .andExpect(jsonPath("validationMessages", hasSize(0)))
                .andExpect(jsonPath("contractResults", hasSize(0)))
                .andExpect(jsonPath("storageResults", hasSize(1)))
                .andExpect(jsonPath("storageResults[0].storageName", is("queryOnly")))
                .andExpect(jsonPath("storageResults[0].revisionNumber", is(1)))
                .andExpect(jsonPath("storageResults[0].storageResultValues", hasSize(2)))
                .andExpect(jsonPath("storageResults[0].storageResultValues[0].dateTimeStart", is("2023-01-01T10:00:00")))
                .andExpect(jsonPath("storageResults[0].storageResultValues[0].dateTimeEnd", is("2023-01-01T10:15:00")))
                .andExpect(jsonPath("storageResults[0].storageResultValues[0].charge", is(10.0)))
                .andExpect(jsonPath("storageResults[0].storageResultValues[0].discharge", is(0.0)))
                .andExpect(jsonPath("storageResults[0].storageResultValues[0].energy", is(30.0)))
                .andExpect(jsonPath("storageResults[0].storageResultValues[0].storageMode", is("CHARGING")))
                .andExpect(jsonPath("storageResults[0].storageResultValues[1].dateTimeStart", is("2023-01-01T10:15:00")))
                .andExpect(jsonPath("storageResults[0].storageResultValues[1].dateTimeEnd", is("2023-01-01T10:30:00")))
                .andExpect(jsonPath("storageResults[0].storageResultValues[1].charge", is(0.0)))
                .andExpect(jsonPath("storageResults[0].storageResultValues[1].discharge", is(5.0)))
                .andExpect(jsonPath("storageResults[0].storageResultValues[1].energy", is(25.0)))
                .andExpect(jsonPath("storageResults[0].storageResultValues[1].storageMode", is("DISCHARGING")));
    }

    @Test
    public void taskResultNotExists() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/tasks/{name}/result", "queryOnly"))
                .andExpect(status().isNotFound());
    }

    private TaskDto getTaskDto(String taskName, LocalDateTime dateTimeStart, LocalDateTime dateTimeEnd) {

        Set<TaskBaseObjectRevisionDto> demandRevisionsDto = new HashSet<>();
        demandRevisionsDto.add(new TaskBaseObjectRevisionDto("queryOnly", 1));

        Set<TaskBaseObjectRevisionDto> contractRevisionsDto = new HashSet<>();
        contractRevisionsDto.add(new TaskBaseObjectRevisionDto("queryOnly", 1));

        return new TaskDto(taskName, dateTimeStart, dateTimeEnd,
                demandRevisionsDto,
                Collections.emptySet(),
                Collections.emptySet(),
                contractRevisionsDto,
                Collections.emptySet(),
                Collections.emptySet()
        );
    }
}
