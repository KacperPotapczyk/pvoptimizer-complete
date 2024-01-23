package com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.service.validation;

import com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskContractConstraintDto;
import com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskContractDto;
import com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.message.ObjectType;
import com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.message.ValidationMessage;
import com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.message.ValidationMessageLevel;
import com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.message.ValidationMessages;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class ContractValidation {

    public void validate(List<TaskContractDto> taskContractDtoList, ValidationMessages.ValidationMessagesBuilder builder) {

        checkContractIds(builder, taskContractDtoList);
        taskContractDtoList.forEach(taskContractDto -> checkContract(builder, taskContractDto));
    }

    private void checkContractIds(ValidationMessages.ValidationMessagesBuilder builder, List<TaskContractDto> taskContractDtoList) {

        Set<Long> uniqueIds = new HashSet<>(taskContractDtoList.size());
        for (TaskContractDto taskContractDto : taskContractDtoList) {

            if (!uniqueIds.add(taskContractDto.getId())) {
                builder.message(new ValidationMessage(
                        ValidationMessageLevel.ERROR,
                        ObjectType.CONTRACT,
                        taskContractDto.getName().toString(),
                        taskContractDto.getId(),
                        taskContractDto.getRevisionNumber(),
                        "Not unique base object id: " + taskContractDto.getId()));
            }
        }
    }

    private void checkContract(ValidationMessages.ValidationMessagesBuilder builder, TaskContractDto taskContractDto) {

        ValidationMessage errorMessageTemplate = ValidationMessage.errorMessageTemplate(
                ObjectType.CONTRACT,
                taskContractDto.getName().toString(),
                taskContractDto.getId(),
                taskContractDto.getRevisionNumber()
        );

        checkContractConstraint("min power", errorMessageTemplate, builder, taskContractDto.getMinPowerConstraints());
        checkContractConstraint("max power", errorMessageTemplate, builder, taskContractDto.getMaxPowerConstraints());
        checkContractConstraint("min energy", errorMessageTemplate, builder, taskContractDto.getMinEnergyConstraints());
        checkContractConstraint("max energy", errorMessageTemplate, builder, taskContractDto.getMaxEnergyConstraints());
    }

    private void checkContractConstraint(
            String constraintType,
            ValidationMessage messageTemplate,
            ValidationMessages.ValidationMessagesBuilder builder,
            List<TaskContractConstraintDto> taskContractConstraintDtoList) {

        taskContractConstraintDtoList.forEach(constraint -> {
            if (constraint.getConstraintValue() < 0.0) {
                builder.message(messageTemplate.withMessage("Negative " + constraintType + " constraint value"));
            }
        });
    }
}
