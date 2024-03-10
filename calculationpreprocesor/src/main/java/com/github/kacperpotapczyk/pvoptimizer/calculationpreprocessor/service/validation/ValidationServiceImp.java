package com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.service.validation;

import com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.*;
import com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.message.ObjectType;
import com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.message.ValidationMessage;
import com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.message.ValidationMessageLevel;
import com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.message.ValidationMessages;
import com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.service.preprocess.utils.DateTimeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ValidationServiceImp implements ValidationService {

    private final DemandValidation demandValidationService;
    private final ContractValidation contractValidationService;
    private final TariffValidation tariffValidationService;
    private final ProductionValidation productionValidation;
    private final StorageValidation storageValidation;
    private final MovableDemandValidation movableDemandValidation;
    private final DateTimeMapper dateTimeMapper;

    @Autowired
    public ValidationServiceImp(DemandValidation demandValidation, ContractValidation contractValidation, TariffValidation tariffValidation, ProductionValidation productionValidation, StorageValidation storageValidation, MovableDemandValidation movableDemandValidation, DateTimeMapper dateTimeMapper) {
        this.demandValidationService = demandValidation;
        this.contractValidationService = contractValidation;
        this.tariffValidationService =  tariffValidation;
        this.productionValidation = productionValidation;
        this.storageValidation = storageValidation;
        this.movableDemandValidation = movableDemandValidation;
        this.dateTimeMapper = dateTimeMapper;
    }

    @Override
    public ValidationMessages validate(TaskCalculationDto taskCalculationDto) {

        long taskId = taskCalculationDto.getId();
        log.debug("Validating task with id: {}", taskId);

        ValidationMessages.ValidationMessagesBuilder builder = ValidationMessages.builder();
        builder.taskId(taskId);

        demandValidationService.validate(taskCalculationDto.getDemands(), builder);
        productionValidation.validate(taskCalculationDto.getProductions(), builder);
        contractValidationService.validate(taskCalculationDto.getContracts(), builder);
        tariffValidationService.validate(taskCalculationDto.getTariffs(), builder);
        validateContractTariffMatching(taskCalculationDto.getContracts(), taskCalculationDto.getTariffs(), builder);
        storageValidation.validate(taskCalculationDto.getStorages(), builder);
        movableDemandValidation.validate(taskCalculationDto.getMovableDemands(), builder);
        validateMovableDemandsTaskHorizonCoverage(taskCalculationDto, builder);

        return builder.build();
    }

    private void validateContractTariffMatching(
            List<TaskContractDto> taskContractDtoList,
            List<TaskTariffDto> taskTariffDtoList,
            ValidationMessages.ValidationMessagesBuilder builder) {

        validateEachContractHasTariff(taskContractDtoList, taskTariffDtoList, builder);
        findUnusedTariffs(taskContractDtoList, taskTariffDtoList, builder);
    }

    private void validateEachContractHasTariff(
            List<TaskContractDto> taskContractDtoList,
            List<TaskTariffDto> taskTariffDtoList,
            ValidationMessages.ValidationMessagesBuilder builder) {

        Set<CharSequence> tariffs = taskTariffDtoList.stream()
                .map(TaskTariffDto::getName)
                .collect(Collectors.toSet());

        taskContractDtoList.forEach(taskContractDto -> {
            if (!tariffs.contains(taskContractDto.getTariffName())) {
                builder.message(new ValidationMessage(
                        ValidationMessageLevel.ERROR,
                        ObjectType.CONTRACT,
                        taskContractDto.getName().toString(),
                        taskContractDto.getId(),
                        taskContractDto.getRevisionNumber(),
                        "Tariff " + taskContractDto.getTariffName() + " is missing."
                ));
            }
        });
    }

    private void findUnusedTariffs(
            List<TaskContractDto> taskContractDtoList,
            List<TaskTariffDto> taskTariffDtoList,
            ValidationMessages.ValidationMessagesBuilder builder) {

        Set<CharSequence> contractTariffs = taskContractDtoList.stream()
                .map(TaskContractDto::getTariffName)
                .collect(Collectors.toSet());

        taskTariffDtoList.forEach(taskTariffDto -> {
            if (!contractTariffs.contains(taskTariffDto.getName())) {
                builder.message(new ValidationMessage(
                        ValidationMessageLevel.INFO,
                        ObjectType.TARIFF,
                        taskTariffDto.getName().toString(),
                        taskTariffDto.getId(),
                        taskTariffDto.getRevisionNumber(),
                        "Tariff is not used."
                ));
            }
        });
    }

    private void validateMovableDemandsTaskHorizonCoverage(
            TaskCalculationDto taskCalculationDto,
            ValidationMessages.ValidationMessagesBuilder builder
    ) {
        LocalDateTime taskStart = dateTimeMapper.mapToLocalDateTime(taskCalculationDto.getDateTimeStart());
        LocalDateTime taskEnd = dateTimeMapper.mapToLocalDateTime(taskCalculationDto.getDateTimeEnd());

        taskCalculationDto.getMovableDemands().forEach(taskMovableDemandDto -> checkMovableDemandTaskHorizonCoverage(
                builder,
                taskMovableDemandDto,
                taskStart,
                taskEnd
        ));
    }

    private void checkMovableDemandTaskHorizonCoverage(
            ValidationMessages.ValidationMessagesBuilder builder,
            TaskMovableDemandDto taskMovableDemandDto,
            LocalDateTime taskStart,
            LocalDateTime taskEnd
    ) {

        long profileLengthInMinutes = taskMovableDemandDto.getMovableDemandValues().stream()
                .map(TaskMovableDemandValueDto::getDurationMinutes)
                .reduce(0L, Long::sum);

        boolean isAllOptionsOutsideTaskHorizon = true;

        for (TaskMovableDemandStartDto taskMovableDemandStartDto : taskMovableDemandDto.getMovableDemandStarts()) {

            LocalDateTime movableDemandStart = dateTimeMapper.mapToLocalDateTime(taskMovableDemandStartDto.getStart());
            LocalDateTime movableDemandEnd = movableDemandStart.plusMinutes(profileLengthInMinutes);

            if (movableDemandStart.isAfter(taskEnd) || movableDemandStart.isEqual(taskEnd)) {
                builder.message(new ValidationMessage(
                        ValidationMessageLevel.WARN,
                        ObjectType.MOVABLE_DEMAND,
                        taskMovableDemandDto.getName().toString(),
                        taskMovableDemandDto.getId(),
                        taskMovableDemandDto.getRevisionNumber(),
                        "Movable demand start option: " + movableDemandStart + " begins after task horizon end. This option has no effect."
                ));
            } else if (movableDemandEnd.isBefore(taskStart) || movableDemandEnd.isEqual(taskStart)) {
                builder.message(new ValidationMessage(
                        ValidationMessageLevel.WARN,
                        ObjectType.MOVABLE_DEMAND,
                        taskMovableDemandDto.getName().toString(),
                        taskMovableDemandDto.getId(),
                        taskMovableDemandDto.getRevisionNumber(),
                        "Movable demand start option: " + movableDemandStart + " ends before task horizon start. This option has no effect."
                ));
            } else if (movableDemandStart.isBefore(taskStart)) {
                builder.message(new ValidationMessage(
                        ValidationMessageLevel.WARN,
                        ObjectType.MOVABLE_DEMAND,
                        taskMovableDemandDto.getName().toString(),
                        taskMovableDemandDto.getId(),
                        taskMovableDemandDto.getRevisionNumber(),
                        "Movable demand start option: " + movableDemandStart + " starts before task horizon start. This option has no effect."
                ));
            } else {
                isAllOptionsOutsideTaskHorizon = false;
            }

            if (movableDemandEnd.isAfter(taskEnd)) {
                builder.message(new ValidationMessage(
                        ValidationMessageLevel.WARN,
                        ObjectType.MOVABLE_DEMAND,
                        taskMovableDemandDto.getName().toString(),
                        taskMovableDemandDto.getId(),
                        taskMovableDemandDto.getRevisionNumber(),
                        "Movable demand start option: " + movableDemandStart + " ends after task horizon end. Profile will be trimmed."
                ));
            }
        }

        if (isAllOptionsOutsideTaskHorizon) {
            builder.message(new ValidationMessage(
                    ValidationMessageLevel.ERROR,
                    ObjectType.MOVABLE_DEMAND,
                    taskMovableDemandDto.getName().toString(),
                    taskMovableDemandDto.getId(),
                    taskMovableDemandDto.getRevisionNumber(),
                    "All movable demand options are outside task horizon."
            ));
        }
    }
}
