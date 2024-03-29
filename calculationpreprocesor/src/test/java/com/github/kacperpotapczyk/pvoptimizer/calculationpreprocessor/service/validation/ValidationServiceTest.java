package com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.service.validation;

import com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskCalculationDto;
import com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.message.ObjectType;
import com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.message.ValidationMessage;
import com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.message.ValidationMessageLevel;
import com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.message.ValidationMessages;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class ValidationServiceTest {

    @Autowired
    private ValidationService validationService;

    @Test
    public void validateNoMessages() throws IOException {

        TaskCalculationDto taskCalculationDto = getTaskCalculationDtoFromFile("TaskCalculationDto/TaskWithContract.json");

        ValidationMessages validationMessages = validationService.validate(taskCalculationDto);
        assertEquals(3, validationMessages.getTaskId());
        assertFalse(validationMessages.hasErrorMessage());
        assertEquals(0, validationMessages.getMessages().size());
    }

    @Test
    public void contractTariffMismatch() throws IOException {

        TaskCalculationDto taskCalculationDto = getTaskCalculationDtoFromFile("TaskCalculationDto/TaskWithContractTariffMismatch.json");

        ValidationMessages validationMessages = validationService.validate(taskCalculationDto);
        assertEquals(4, validationMessages.getTaskId());
        assertTrue(validationMessages.hasErrorMessage());
        assertEquals(2, validationMessages.getMessages().size());

        ValidationMessage errorMessage = validationMessages.getMessages().stream()
                .filter(message -> message.level() == ValidationMessageLevel.ERROR)
                .findAny()
                .orElseThrow();

        assertEquals(ObjectType.CONTRACT, errorMessage.objectType());
        assertEquals("contract1", errorMessage.baseObjectName());
        assertEquals(1, errorMessage.id());
        assertEquals(1, errorMessage.revisionId());
        assertEquals("Tariff tariff1 is missing.", errorMessage.message());

        ValidationMessage infoMessage = validationMessages.getMessages().stream()
                .filter(message -> message.level() == ValidationMessageLevel.INFO)
                .findAny()
                .orElseThrow();

        assertEquals(ObjectType.TARIFF, infoMessage.objectType());
        assertEquals("other tariff", infoMessage.baseObjectName());
        assertEquals(2, infoMessage.id());
        assertEquals(1, infoMessage.revisionId());
        assertEquals("Tariff is not used.", infoMessage.message());
    }

    @Test
    public void validateMovableDemandTaskHorizonCoverage() throws IOException {

        TaskCalculationDto taskCalculationDto = getTaskCalculationDtoFromFile("TaskCalculationDto/TaskWithMovableDemand.json");
        ValidationMessages validationMessages = validationService.validate(taskCalculationDto);

        assertEquals(8, validationMessages.getTaskId());
        assertFalse(validationMessages.hasErrorMessage());
        assertEquals(2, validationMessages.getMessages().size());

        ValidationMessage validationMessage1 = validationMessages.getMessages().get(0);
        assertEquals(2, validationMessage1.id());
        assertEquals(3, validationMessage1.revisionId());
        assertEquals("movableDemand1", validationMessage1.baseObjectName());
        assertEquals("Movable demand start option: 2023-01-01T09:55 starts before task horizon start. This option has no effect.", validationMessage1.message());

        ValidationMessage validationMessage2 = validationMessages.getMessages().get(1);
        assertEquals(2, validationMessage2.id());
        assertEquals(3, validationMessage2.revisionId());
        assertEquals("movableDemand1", validationMessage2.baseObjectName());
        assertEquals("Movable demand start option: 2023-01-01T10:30 ends after task horizon end. Profile will be trimmed.", validationMessage2.message());
    }

    @Test
    public void duplicatedIds() throws IOException {

        TaskCalculationDto taskCalculationDto = getTaskCalculationDtoFromFile("TaskCalculationDto/TaskWithDuplicatedIds.json");

        ValidationMessages validationMessages = validationService.validate(taskCalculationDto);
        assertEquals(5, validationMessages.getTaskId());
        assertTrue(validationMessages.hasErrorMessage());
        assertEquals(6, validationMessages.getMessages().size());

        ValidationMessage errorDemand = validationMessages.getMessages().stream()
                .filter(validationMessage -> validationMessage.objectType() == ObjectType.DEMAND)
                .findAny()
                .orElseThrow();

        assertEquals(ObjectType.DEMAND, errorDemand.objectType());
        assertEquals(4, errorDemand.id());
        assertEquals(1, errorDemand.revisionId());
        assertEquals("Not unique base object id: 4", errorDemand.message());

        ValidationMessage errorProduction = validationMessages.getMessages().stream()
                .filter(validationMessage -> validationMessage.objectType() == ObjectType.PRODUCTION)
                .findAny()
                .orElseThrow();

        assertEquals(ObjectType.PRODUCTION, errorProduction.objectType());
        assertEquals(10, errorProduction.id());
        assertEquals(1, errorProduction.revisionId());
        assertEquals("Not unique base object id: 10", errorProduction.message());

        ValidationMessage errorContract = validationMessages.getMessages().stream()
                .filter(validationMessage -> validationMessage.objectType() == ObjectType.CONTRACT)
                .findAny()
                .orElseThrow();

        assertEquals(ObjectType.CONTRACT, errorContract.objectType());
        assertEquals(1, errorContract.id());
        assertEquals("Not unique base object id: 1", errorContract.message());

        ValidationMessage errorTariff = validationMessages.getMessages().stream()
                .filter(validationMessage -> validationMessage.objectType() == ObjectType.TARIFF)
                .findAny()
                .orElseThrow();

        assertEquals(ObjectType.TARIFF, errorTariff.objectType());
        assertEquals(2, errorTariff.id());
        assertEquals("Not unique base object id: 2", errorTariff.message());

        ValidationMessage errorStorage = validationMessages.getMessages().stream()
                .filter(validationMessage -> validationMessage.objectType() == ObjectType.STORAGE)
                .findAny()
                .orElseThrow();
        assertEquals(ObjectType.STORAGE, errorStorage.objectType());
        assertEquals(44, errorStorage.id());
        assertEquals("Not unique base object id: 44", errorStorage.message());

        ValidationMessage errorMovableDemand = validationMessages.getMessages().stream()
                .filter(validationMessage -> validationMessage.objectType() == ObjectType.MOVABLE_DEMAND)
                .findAny()
                .orElseThrow();
        assertEquals(ObjectType.MOVABLE_DEMAND, errorMovableDemand.objectType());
        assertEquals(69, errorMovableDemand.id());
        assertEquals("Not unique base object id: 69", errorMovableDemand.message());
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