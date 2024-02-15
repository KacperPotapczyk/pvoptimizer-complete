package com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.service.validation;

import com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.*;
import com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.message.ObjectType;
import com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.message.ValidationMessage;
import com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.message.ValidationMessageLevel;
import com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.message.ValidationMessages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class StorageValidation {

    public void validate(List<TaskStorageDto> taskStorageDtoList, ValidationMessages.ValidationMessagesBuilder builder) {

        checkStorageIds(builder, taskStorageDtoList);
        taskStorageDtoList.forEach(taskStorageDto -> checkStorage(builder, taskStorageDto));
    }

    private void checkStorageIds(ValidationMessages.ValidationMessagesBuilder builder, List<TaskStorageDto> taskStorageDtoList) {

        Set<Long> uniqueIds = new HashSet<>(taskStorageDtoList.size());

        for (TaskStorageDto taskStorageDto : taskStorageDtoList) {

            if (!uniqueIds.add(taskStorageDto.getId())) {
                builder.message(new ValidationMessage(
                        ValidationMessageLevel.ERROR,
                        ObjectType.STORAGE,
                        taskStorageDto.getName().toString(),
                        taskStorageDto.getId(),
                        taskStorageDto.getRevisionNumber(),
                        "Not unique base object id: " + taskStorageDto.getId()));
            }
        }
    }

    private void checkStorage(ValidationMessages.ValidationMessagesBuilder builder, TaskStorageDto taskStorageDto) {

        ValidationMessage errorMessageTemplate = ValidationMessage.errorMessageTemplate(
                ObjectType.STORAGE,
                taskStorageDto.getName().toString(),
                taskStorageDto.getId(),
                taskStorageDto.getRevisionNumber()
        );

        if (taskStorageDto.getCapacity() < 0.0) {
            builder.message(errorMessageTemplate.withMessage("Negative capacity value"));
        }
        if (taskStorageDto.getMaxCharge() < 0.0) {
            builder.message(errorMessageTemplate.withMessage("Negative max charging value"));
        }
        if (taskStorageDto.getMaxDischarge() < 0.0) {
            builder.message(errorMessageTemplate.withMessage("Negative max discharging value"));
        }
        if (taskStorageDto.getInitialEnergy() < 0.0) {
            builder.message(errorMessageTemplate.withMessage("Negative initial energy value"));
        }

        checkContractConstraint("min charge", errorMessageTemplate, builder, taskStorageDto.getMinChargeConstraints());
        checkContractConstraint("max charge", errorMessageTemplate, builder, taskStorageDto.getMaxChargeConstraints());
        checkContractConstraint("min discharge", errorMessageTemplate, builder, taskStorageDto.getMinDischargeConstraints());
        checkContractConstraint("max discharge", errorMessageTemplate, builder, taskStorageDto.getMaxDischargeConstraints());
        checkContractConstraint("min energy", errorMessageTemplate, builder, taskStorageDto.getMinEnergyConstraints());
        checkContractConstraint("max energy", errorMessageTemplate, builder, taskStorageDto.getMaxEnergyConstraints());
    }

    private void checkContractConstraint(
            String constraintType,
            ValidationMessage messageTemplate,
            ValidationMessages.ValidationMessagesBuilder builder,
            List<TaskStorageConstraintDto> taskStorageConstraintDtoList) {

        taskStorageConstraintDtoList.forEach(constraint -> {
            if (constraint.getConstraintValue() < 0.0) {
                builder.message(messageTemplate.withMessage("Negative " + constraintType + " constraint value"));
            }
        });
    }
}
