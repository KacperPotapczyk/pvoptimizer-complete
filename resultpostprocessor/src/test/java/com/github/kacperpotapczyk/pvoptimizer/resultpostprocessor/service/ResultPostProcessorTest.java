package com.github.kacperpotapczyk.pvoptimizer.resultpostprocessor.service;

import com.github.kacperpotapczyk.pvoptimizer.avro.postprocessor.TaskPostProcessDataDto;
import com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.result.*;
import com.github.kacperpotapczyk.pvoptimizer.avro.optimizer.result.ResultDto;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.apache.commons.io.FileUtils;
import org.springframework.test.context.ActiveProfiles;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@ActiveProfiles("test")
class ResultPostProcessorTest {

    @Autowired
    private ResultPostProcessor resultPostProcessor;

    @Test
    public void successfulContract() throws IOException {

        TaskPostProcessDataDto taskPostProcessDataDto = getTaskPostProcessDataDtoFromFile("TaskPostProcessDataDto/successfulContract.json");
        ResultDto resultDto = getResultDtoFromFile("ResultDto/successfulContract.json");

        TaskCalculationResultDto taskCalculationResult = resultPostProcessor.postProcess(taskPostProcessDataDto, resultDto);

        assertEquals(5, taskCalculationResult.getId());
        assertEquals("2023-01-01T10:00", taskCalculationResult.getDateTimeStart().toString());
        assertEquals("2023-01-01T10:30", taskCalculationResult.getDateTimeEnd().toString());

        assertEquals(TaskCalculationResultStatusDto.SOLUTION_FOUND, taskCalculationResult.getResultStatus());
        assertEquals(0.41666666666666663, taskCalculationResult.getObjectiveFunctionValue(), 1e-6);
        assertEquals(1e-10, taskCalculationResult.getRelativeGap(), 1e-6);
        assertEquals(0, taskCalculationResult.getElapsedTime(), 1e-6);
        assertEquals("", taskCalculationResult.getOptimizerMessage().toString());
        assertEquals(1, taskCalculationResult.getContractResults().size());

        TaskCalculationContractResultDto contractResult = taskCalculationResult.getContractResults().get(0);
        assertEquals(2, contractResult.getId());
        assertEquals(2, contractResult.getContractResultValues().size());

        TaskCalculationContractResultValueDto contractResultValue1 = contractResult.getContractResultValues().get(0);
        assertEquals("2023-01-01T10:00:00", contractResultValue1.getDateTimeStart().toString());
        assertEquals("2023-01-01T10:15:00", contractResultValue1.getDateTimeEnd().toString());
        assertEquals(10.0, contractResultValue1.getPower(), 1e-6);
        assertEquals(2.5, contractResultValue1.getEnergy(), 1e-6);
        assertEquals(0.125, contractResultValue1.getCost(), 1e-6);

        TaskCalculationContractResultValueDto contractResultValue2 = contractResult.getContractResultValues().get(1);
        assertEquals("2023-01-01T10:15:00", contractResultValue2.getDateTimeStart().toString());
        assertEquals("2023-01-01T10:30:00", contractResultValue2.getDateTimeEnd().toString());
        assertEquals(23.333333333344, contractResultValue2.getPower(), 1e-6);
        assertEquals(5.833333333336, contractResultValue2.getEnergy(), 1e-6);
        assertEquals(0.2916666666668, contractResultValue2.getCost(), 1e-6);
    }

    @Test
    public void successfulStorage() throws IOException {

        TaskPostProcessDataDto taskPostProcessDataDto = getTaskPostProcessDataDtoFromFile("TaskPostProcessDataDto/successfulStorage.json");
        ResultDto resultDto = getResultDtoFromFile("ResultDto/successfulStorage.json");

        TaskCalculationResultDto taskCalculationResult = resultPostProcessor.postProcess(taskPostProcessDataDto, resultDto);

        assertEquals(6, taskCalculationResult.getId());
        assertEquals("2023-01-01T10:00:00", taskCalculationResult.getDateTimeStart().toString());
        assertEquals("2023-01-01T10:30:00", taskCalculationResult.getDateTimeEnd().toString());
        assertEquals(1, taskCalculationResult.getStorageResults().size());

        TaskCalculationStorageResultDto storageResult = taskCalculationResult.getStorageResults().get(0);
        assertEquals(1, storageResult.getId());
        assertEquals(2, storageResult.getStorageResultValues().size());

        TaskCalculationStorageResultValueDto storageResultValue1 = storageResult.getStorageResultValues().get(0);
        assertEquals("2023-01-01T10:00:00", storageResultValue1.getDateTimeStart().toString());
        assertEquals("2023-01-01T10:15:00", storageResultValue1.getDateTimeEnd().toString());
        assertEquals(0.0, storageResultValue1.getCharge(), 1e-6);
        assertEquals(8.33333333333333, storageResultValue1.getDischarge(), 1e-6);
        assertEquals(27.916666666666668, storageResultValue1.getEnergy(), 1e-6);
        assertEquals(TaskCalculationStorageModeDto.DISCHARGING, storageResultValue1.getStorageMode());

        TaskCalculationStorageResultValueDto storageResultValue2 = storageResult.getStorageResultValues().get(1);
        assertEquals("2023-01-01T10:15:00", storageResultValue2.getDateTimeStart().toString());
        assertEquals("2023-01-01T10:30:00", storageResultValue2.getDateTimeEnd().toString());
        assertEquals(0.0, storageResultValue2.getCharge(), 1e-6);
        assertEquals(20.0, storageResultValue2.getDischarge(), 1e-6);
        assertEquals(22.916666666666668, storageResultValue2.getEnergy(), 1e-6);
        assertEquals(TaskCalculationStorageModeDto.DISCHARGING, storageResultValue2.getStorageMode());
    }

    @Test
    public void successfulMovableDemand() throws IOException {

        TaskPostProcessDataDto taskPostProcessDataDto = getTaskPostProcessDataDtoFromFile("TaskPostProcessDataDto/successfulMovableDemand.json");
        ResultDto resultDto = getResultDtoFromFile("ResultDto/successfulMovableDemand.json");

        TaskCalculationResultDto taskCalculationResult = resultPostProcessor.postProcess(taskPostProcessDataDto, resultDto);

        assertEquals(2, taskCalculationResult.getId());
        assertEquals("2023-01-01T10:00:00", taskCalculationResult.getDateTimeStart().toString());
        assertEquals("2023-01-01T10:30:00", taskCalculationResult.getDateTimeEnd().toString());
        assertEquals(1, taskCalculationResult.getMovableDemandResults().size());

        TaskCalculationMovableDemandResultDto movableDemandResultDto = taskCalculationResult.getMovableDemandResults().get(0);
        assertEquals(1, movableDemandResultDto.getId());
        assertEquals(1, movableDemandResultDto.getMovableDemandResultValues().size());
        assertEquals("2023-01-01T10:15:00", movableDemandResultDto.getMovableDemandResultValues().get(0).getDateTimeStart().toString());
        assertEquals("2023-01-01T10:30:00", movableDemandResultDto.getMovableDemandResultValues().get(0).getDateTimeEnd().toString());
        assertEquals(10.0, movableDemandResultDto.getMovableDemandResultValues().get(0).getPower(), 1e-6);
        assertEquals(2.5, movableDemandResultDto.getMovableDemandResultValues().get(0).getEnergy(), 1e-6);
    }

    @Test
    public void solutionNotFound() throws IOException {

        TaskPostProcessDataDto taskPostProcessDataDto = getTaskPostProcessDataDtoFromFile("TaskPostProcessDataDto/solutionNotFound.json");
        ResultDto resultDto = getResultDtoFromFile("ResultDto/solutionNotFound.json");

        TaskCalculationResultDto taskCalculationResult = resultPostProcessor.postProcess(taskPostProcessDataDto, resultDto);

        assertEquals(3, taskCalculationResult.getId());
        assertEquals("2023-01-01T10:00", taskCalculationResult.getDateTimeStart().toString());
        assertEquals("2023-01-01T10:30", taskCalculationResult.getDateTimeEnd().toString());

        assertEquals(TaskCalculationResultStatusDto.SOLUTION_NOT_FOUND, taskCalculationResult.getResultStatus());
        assertNull(taskCalculationResult.getObjectiveFunctionValue());
        assertNull(taskCalculationResult.getRelativeGap());
        assertNull(taskCalculationResult.getElapsedTime());
        assertEquals("Solution could not be found.", taskCalculationResult.getOptimizerMessage().toString());

        assertEquals(0, taskCalculationResult.getContractResults().size());
    }

    private TaskPostProcessDataDto getTaskPostProcessDataDtoFromFile(String fileName) throws IOException {

        File file = new ClassPathResource(fileName).getFile();
        byte[] fileContent = FileUtils.readFileToByteArray(file);
        DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(fileContent));

        DatumReader<TaskPostProcessDataDto> reader = new SpecificDatumReader<>(TaskPostProcessDataDto.class);
        Decoder decoder = DecoderFactory.get().jsonDecoder(TaskPostProcessDataDto.getClassSchema(), dataInputStream);
        return reader.read(null, decoder);
    }

    private ResultDto getResultDtoFromFile(String fileName) throws IOException {

        File file = new ClassPathResource(fileName).getFile();
        byte[] fileContent = FileUtils.readFileToByteArray(file);
        DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(fileContent));

        DatumReader<ResultDto> reader = new SpecificDatumReader<>(ResultDto.class);
        Decoder decoder = DecoderFactory.get().jsonDecoder(ResultDto.getClassSchema(), dataInputStream);
        return reader.read(null, decoder);
    }
}