package com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.service.preprocess;

import com.github.kacperpotapczyk.pvoptimizer.avro.optimizer.task.*;
import com.github.kacperpotapczyk.pvoptimizer.avro.postprocessor.TaskPostProcessDataDto;
import com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskCalculationDto;
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

        assertEquals(2, taskDto.getProduction().getProductionProfile().size());
        assertEquals(6.666666667, taskDto.getProduction().getProductionProfile().get(0), 1e-6);
        assertEquals(13.333333333, taskDto.getProduction().getProductionProfile().get(1), 1e-6);

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

    @Test
    public void mapTaskWithStorage() throws IOException {

        TaskCalculationDto taskCalculationDto = getTaskCalculationDtoFromFile("TaskCalculationDto/TaskWithStorage.json");
        PreProcessResult preProcessResult = taskPreProcess.preProcess(taskCalculationDto);

        TaskDto taskDto = preProcessResult.taskDto();
        assertEquals(1, taskDto.getStorages().size());

        StorageDto storageDto = taskDto.getStorages().get(0);
        assertEquals(44, storageDto.getId());
        assertEquals("storage1", storageDto.getName().toString());
        assertEquals(100.0, storageDto.getMaxCapacity());
        assertEquals(10.0, storageDto.getMaxCharge());
        assertEquals(20.0, storageDto.getMaxDischarge());
        assertEquals(40.0, storageDto.getInitialEnergy());

        assertEquals(1, storageDto.getMinChargeConstraints().size());
        assertEquals(0.5, storageDto.getMinChargeConstraints().get("0"), 1e-9);

        assertEquals(0, storageDto.getMaxChargeConstraints().size());
        assertEquals(0, storageDto.getMinDischargeConstraints().size());
        assertEquals(0, storageDto.getMaxDischargeConstraints().size());

        assertEquals(1, storageDto.getMinEnergyConstraints().size());
        assertEquals(10.0, storageDto.getMinEnergyConstraints().get("0"), 1e-9);

        assertEquals(1, storageDto.getMaxEnergyConstraints().size());
        assertEquals(80.0, storageDto.getMaxEnergyConstraints().get("1"), 1e-9);
    }

    @Test
    public void mapTaskWithCyclicTariff() throws IOException {

        TaskCalculationDto taskCalculationDto = getTaskCalculationDtoFromFile("TaskCalculationDto/TaskWithCyclicTariff.json");
        PreProcessResult preProcessResult = taskPreProcess.preProcess(taskCalculationDto);
        TaskDto taskDto = preProcessResult.taskDto();

        assertEquals(3, taskDto.getIntervals().size());
        assertEquals(0.25, taskDto.getIntervals().get(0));
        assertEquals(0.25, taskDto.getIntervals().get(1));
        assertEquals(0.166666666666667, taskDto.getIntervals().get(2), 1e-6);


        assertEquals(1, taskDto.getContracts().size());
        ContractDto contractDto = taskDto.getContracts().get(0);
        assertEquals("contract2", contractDto.getName().toString());
        assertEquals(1, contractDto.getId());
        assertEquals(ContractDirectionDto.PURCHASE, contractDto.getContractDirection());

        assertEquals(3, contractDto.getUnitPrice().size());
        assertEquals(0.166666666666667, contractDto.getUnitPrice().get(0), 1e-6);
        assertEquals(0.133333333333333, contractDto.getUnitPrice().get(1), 1e-6);
        assertEquals(0.2, contractDto.getUnitPrice().get(2), 1e-6);
    }

    @Test
    public void mapTaskWithMovableDemand() throws IOException {

        TaskCalculationDto taskCalculationDto = getTaskCalculationDtoFromFile("TaskCalculationDto/TaskWithMovableDemand.json");
        PreProcessResult preProcessResult = taskPreProcess.preProcess(taskCalculationDto);
        TaskDto taskDto = preProcessResult.taskDto();

        assertEquals(3, taskDto.getIntervals().size());
        assertEquals(1, taskDto.getMovableDemands().size());

        MovableDemandDto movableDemandDto = taskDto.getMovableDemands().get(0);
        assertEquals(2, movableDemandDto.getId());
        assertEquals("movableDemand1", movableDemandDto.getName().toString());

        List<Double> profile = movableDemandDto.getProfile();
        assertEquals(2, profile.size());
        assertEquals(8.333333333, profile.get(0), 1e-6);
        assertEquals(1.666666667, profile.get(1), 1e-6);

        List<Integer> startIntervals = movableDemandDto.getStartIntervals();
        assertEquals(2, startIntervals.size());
        assertEquals(1, startIntervals.get(0));
        assertEquals(2, startIntervals.get(1));
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