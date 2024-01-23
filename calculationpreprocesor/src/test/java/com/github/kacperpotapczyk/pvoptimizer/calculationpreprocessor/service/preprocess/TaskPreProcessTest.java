package com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.service.preprocess;

import com.github.kacperpotapczyk.pvoptimizer.avro.postprocessor.TaskPostProcessDataDto;
import com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskCalculationDto;
import com.github.kacperpotapczyk.pvoptimizer.avro.optimizer.task.ContractDirectionDto;
import com.github.kacperpotapczyk.pvoptimizer.avro.optimizer.task.ContractDto;
import com.github.kacperpotapczyk.pvoptimizer.avro.optimizer.task.SumConstraintDto;
import com.github.kacperpotapczyk.pvoptimizer.avro.optimizer.task.TaskDto;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class TaskPreProcessTest {

    @Autowired
    private TaskPreProcess taskPreProcess;

    @Test
    void mapTaskWithOnlyDemand() throws IOException {

        TaskCalculationDto taskCalculationDto = getTaskCalculationDtoFromFile("TaskCalculationDto/TaskWithDemandOnly.json");
        PreProcessResult preProcessResult = taskPreProcess.preProcess(taskCalculationDto);
        TaskDto taskDto = preProcessResult.taskDto();

        assertEquals(1, taskDto.getId());
        assertEquals(0, taskDto.getContracts().size());
        assertEquals(0, taskDto.getStorages().size());
        assertEquals(0, taskDto.getMovableDemands().size());

        assertEquals(2, taskDto.getIntervals().size());
        for (Double interval : taskDto.getIntervals()) {
            assertEquals(0.25, interval);
        }

        assertEquals(2, taskDto.getDemand().getDemandProfile().size());
        List<Double> demandProfile = taskDto.getDemand().getDemandProfile();
        assertEquals(10.0, demandProfile.get(0), 1e-9);
        assertEquals(23.333333333, demandProfile.get(1), 1e-9);

        TaskPostProcessDataDto taskPostProcessDataDto = preProcessResult.taskPostProcessDataDto();
        assertEquals(1, taskPostProcessDataDto.getId());
        assertEquals("2023-01-01T10:00", taskPostProcessDataDto.getDateTimeStart().toString());
        assertEquals("2023-01-01T10:30", taskPostProcessDataDto.getDateTimeEnd().toString());
        assertEquals(2, taskPostProcessDataDto.getIntervals().size());
    }

    @Test
    void mapTaskWithTwoDemandsOnly() throws IOException {

        TaskCalculationDto taskCalculationDto = getTaskCalculationDtoFromFile("TaskCalculationDto/TaskWithTwoDemandsOnly.json");
        PreProcessResult preProcessResult = taskPreProcess.preProcess(taskCalculationDto);
        TaskDto taskDto = preProcessResult.taskDto();

        assertEquals(2, taskDto.getId());
        assertEquals(0, taskDto.getContracts().size());
        assertEquals(0, taskDto.getStorages().size());
        assertEquals(0, taskDto.getMovableDemands().size());

        assertEquals(2, taskDto.getIntervals().size());
        for (Double interval : taskDto.getIntervals()) {
            assertEquals(0.25, interval);
        }

        assertEquals(2, taskDto.getDemand().getDemandProfile().size());
        List<Double> demandProfile = taskDto.getDemand().getDemandProfile();
        assertEquals(10.0, demandProfile.get(0), 1e-9);
        assertEquals(26.666666667, demandProfile.get(1), 1e-9);

        TaskPostProcessDataDto taskPostProcessDataDto = preProcessResult.taskPostProcessDataDto();
        assertEquals(2, taskPostProcessDataDto.getId());
        assertEquals("2023-01-01T10:00", taskPostProcessDataDto.getDateTimeStart().toString());
        assertEquals("2023-01-01T10:30", taskPostProcessDataDto.getDateTimeEnd().toString());
        assertEquals(2, taskPostProcessDataDto.getIntervals().size());
    }

    @Test
    void mapTaskWithContract() throws IOException {

        TaskCalculationDto taskCalculationDto = getTaskCalculationDtoFromFile("TaskCalculationDto/TaskWithContract.json");
        PreProcessResult preProcessResult = taskPreProcess.preProcess(taskCalculationDto);
        TaskDto taskDto = preProcessResult.taskDto();

        assertEquals(3, taskDto.getId());
        assertEquals(0, taskDto.getStorages().size());
        assertEquals(0, taskDto.getMovableDemands().size());

        assertEquals(2, taskDto.getIntervals().size());
        for (Double interval : taskDto.getIntervals()) {
            assertEquals(0.25, interval);
        }
        assertEquals(2, taskDto.getDemand().getDemandProfile().size());

        assertEquals(1, taskDto.getContracts().size());
        ContractDto contractDto = taskDto.getContracts().get(0);
        assertEquals("contract1", contractDto.getName().toString());
        assertEquals(1, contractDto.getId());
        assertEquals(ContractDirectionDto.PURCHASE, contractDto.getContractDirection());

        assertEquals(1, contractDto.getMinPower().size());
        assertEquals(0.5, contractDto.getMinPower().get("0"), 1e-9);

        assertEquals(0, contractDto.getMaxPower().size());

        assertEquals(1, contractDto.getMinEnergy().size());
        SumConstraintDto minEnergy = contractDto.getMinEnergy().get(0);
        assertEquals(0, minEnergy.getStartInterval());
        assertEquals(0, minEnergy.getEndInterval());
        assertEquals(5, minEnergy.getSum(), 1e-9);

        assertEquals(1, contractDto.getMaxEnergy().size());
        SumConstraintDto maxEnergy = contractDto.getMaxEnergy().get(0);
        assertEquals(0, maxEnergy.getStartInterval());
        assertEquals(1, maxEnergy.getEndInterval());
        assertEquals(10, maxEnergy.getSum(), 1e-9);

        assertEquals(2, contractDto.getUnitPrice().size());
        for (Double unitPrice : contractDto.getUnitPrice()) {
            assertEquals(0.05, unitPrice, 1e-9);
        }

        TaskPostProcessDataDto taskPostProcessDataDto = preProcessResult.taskPostProcessDataDto();
        assertEquals(3, taskPostProcessDataDto.getId());
        assertEquals("2023-01-01T10:00", taskPostProcessDataDto.getDateTimeStart().toString());
        assertEquals("2023-01-01T10:30", taskPostProcessDataDto.getDateTimeEnd().toString());
        assertEquals(2, taskPostProcessDataDto.getIntervals().size());
    }

    private TaskCalculationDto getTaskCalculationDtoFromFile(String fileName) throws IOException {

        File file = new ClassPathResource(fileName).getFile();
        byte[] fileContent = FileUtils.readFileToByteArray(file);
        DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(fileContent));

        DatumReader<TaskCalculationDto> reader = new SpecificDatumReader<>(TaskCalculationDto.class);
        Decoder decoder = DecoderFactory.get().jsonDecoder(TaskCalculationDto.getClassSchema(), dataInputStream);
        return reader.read(null, decoder);
    }
}