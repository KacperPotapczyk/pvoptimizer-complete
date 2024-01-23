package com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.service.validation;

import com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskCalculationDto;
import com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskContractDto;
import com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskTariffDto;
import com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.message.ObjectType;
import com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.message.ValidationMessage;
import com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.message.ValidationMessageLevel;
import com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.message.ValidationMessages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ValidationServiceImp implements ValidationService {

    private final DemandValidation demandValidationService;
    private final ContractValidation contractValidationService;
    private final TariffValidation tariffValidationService;

    @Autowired
    public ValidationServiceImp(DemandValidation demandValidation, ContractValidation contractValidation, TariffValidation tariffValidation) {
        this.demandValidationService = demandValidation;
        this.contractValidationService = contractValidation;
        this.tariffValidationService =  tariffValidation;
    }

    @Override
    public ValidationMessages validate(TaskCalculationDto taskCalculationDto) {

        long taskId = taskCalculationDto.getId();
        log.debug("Validating task with id: {}", taskId);

        ValidationMessages.ValidationMessagesBuilder builder = ValidationMessages.builder();
        builder.taskId(taskId);

        demandValidationService.validate(taskCalculationDto.getDemands(), builder);
        contractValidationService.validate(taskCalculationDto.getContracts(), builder);
        tariffValidationService.validate(taskCalculationDto.getTariffs(), builder);
        validateContractTariffMatching(taskCalculationDto.getContracts(), taskCalculationDto.getTariffs(), builder);

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
}
