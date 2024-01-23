package com.github.kacperpotapczyk.pvoptimizer.backend.service;

import com.github.kacperpotapczyk.pvoptimizer.backend.entity.contract.ContractRevision;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.demand.DemandRevision;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.result.ResultStatus;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.result.TaskResult;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.tariff.TariffRevision;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.task.Task;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka
public class TaskServiceTest {

    private final TaskService taskService;
    private final TaskResultService resultService;
    private final ContractService contractService;
    private final TariffService tariffService;
    private final DemandService demandService;

    @Autowired
    public TaskServiceTest(TaskService taskService, TaskResultService resultService, ContractService contractService, TariffService tariffService, DemandService demandService) {
        this.taskService = taskService;
        this.resultService = resultService;
        this.contractService = contractService;
        this.tariffService = tariffService;
        this.demandService = demandService;
    }

    @Test
    public void testTaskAddingAndQuery() {

        String taskName = "New task 1";
        LocalDateTime dateTimeStart = LocalDateTime.parse("2023-10-05T11:00:00");
        LocalDateTime dateTimeEnd = LocalDateTime.parse("2023-10-05T23:00:00");

        ContractRevision contractRevision = contractService.getBaseObjectRevision("queryOnly", 1);
        TariffRevision tariffRevision = tariffService.getBaseObjectRevision("queryOnly", 1);
        DemandRevision demandRevision1 = demandService.getBaseObjectRevision("queryOnly", 1);
        DemandRevision demandRevision2 = demandService.getBaseObjectRevision("addRevisionTest", 1);

        Task task = new Task(taskName, dateTimeStart, dateTimeEnd);
        task.addContractRevision(contractRevision);
        task.addTariffRevision(tariffRevision);
        task.addDemandRevision(demandRevision1);
        task.addDemandRevision(demandRevision2);

        Task addedTask = taskService.newTask(task);

        assertEquals(taskName, addedTask.getName());
        assertEquals(1, addedTask.getContractRevisions().size());
        assertEquals(1, addedTask.getTariffRevisions().size());
        assertEquals(2, addedTask.getDemandRevisions().size());

        Task dbTask = taskService.findByName(taskName);

        assertTrue(taskService.existsById(dbTask.getId()));
        assertEquals(dateTimeStart, dbTask.getDateTimeStart());
        assertEquals(dateTimeEnd, dbTask.getDateTimeEnd());
        assertFalse(dbTask.isReadOnly());
    }

    @Test
    @Transactional
    public void updateTask() {

        String taskName = "Task to be updated";
        LocalDateTime dateTimeStart = LocalDateTime.parse("2023-10-05T11:00:00");
        LocalDateTime dateTimeEnd = LocalDateTime.parse("2023-10-05T23:00:00");

        Task task = new Task(taskName, dateTimeStart, dateTimeEnd);

        taskService.newTask(task);

        Task updateTask = taskService.findByName(taskName);
        assertNotNull(updateTask);
        assertNull(updateTask.getContractRevisions());

        ContractRevision contractRevision = contractService.getBaseObjectRevision("queryOnly", 1);
        updateTask.addContractRevision(contractRevision);

        Task afterUpdateTask = taskService.updateTask(updateTask);
        assertEquals(1, afterUpdateTask.getContractRevisions().size());

        Task dbTask = taskService.findByName(taskName);
        assertEquals(taskName, dbTask.getName());
        assertEquals(1, dbTask.getContractRevisions().size());
        assertNull(dbTask.getTariffRevisions());
    }

    @Test
    public void deleteTask() {

        String taskName = "toBeDeleted";

        assertTrue(taskService.existsByName(taskName));

        taskService.deleteTask(taskName);
        assertFalse(taskService.existsByName(taskName));
    }

    @Test
    @Transactional
    public void getTask() {

        String taskName = "queryOnly";
        Task task = taskService.findByName(taskName);

        assertNotNull(task);
        assertEquals(taskName, task.getName());
        assertFalse(task.isReadOnly());
        assertEquals(1, task.getDemandRevisions().size());
        assertEquals(1, task.getTariffRevisions().size());
        assertEquals(2, task.getContractRevisions().size());
    }

    @Test
    @Transactional
    public void sendTaskForCalculations() {

        String taskName = "sendToCompute";
        taskService.sendTaskForComputations(taskName);

        Task task = taskService.findByName(taskName);
        assertEquals(taskName, task.getName());
        assertTrue(task.isReadOnly());

        TaskResult taskResult = resultService.getResultForTaskName(taskName);

        assertEquals(taskName, taskResult.getTask().getName());
        assertEquals(ResultStatus.STARTED, taskResult.getResultStatus());

        assertNull(taskResult.getValidationMessages());
        assertNull(taskResult.getElapsedTime());
        assertNull(taskResult.getRelativeGap());
        assertNull(taskResult.getObjectiveFunctionValue());
        assertNull(taskResult.getOptimizerMessage());
    }
}
