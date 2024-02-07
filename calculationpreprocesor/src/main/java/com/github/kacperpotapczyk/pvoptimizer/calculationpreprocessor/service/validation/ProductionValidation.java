package com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.service.validation;

import com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskProductionDto;
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
public class ProductionValidation {

    public void validate(List<TaskProductionDto> taskProductionDtoList, ValidationMessages.ValidationMessagesBuilder builder) {

        checkProductionIds(builder, taskProductionDtoList);
        taskProductionDtoList.forEach(taskProductionDto -> checkProductionsValues(builder, taskProductionDto));
    }

    private void checkProductionIds(ValidationMessages.ValidationMessagesBuilder builder, List<TaskProductionDto> taskProductionDtoList) {

        Set<Long> uniqueIds = new HashSet<>(taskProductionDtoList.size());

        for (TaskProductionDto taskProductionDto : taskProductionDtoList) {

            if (!uniqueIds.add(taskProductionDto.getId())) {
                builder.message(new ValidationMessage(
                        ValidationMessageLevel.ERROR,
                        ObjectType.PRODUCTION,
                        taskProductionDto.getName().toString(),
                        taskProductionDto.getId(),
                        taskProductionDto.getRevisionNumber(),
                        "Not unique base object id: " + taskProductionDto.getId()));
            }
        }
    }

    private void checkProductionsValues(ValidationMessages.ValidationMessagesBuilder builder, TaskProductionDto taskProductionDto) {

        if (taskProductionDto.getProductionsValues().stream().anyMatch(value -> value.getValue() < 0.0)) {
            builder.message(new ValidationMessage(
                    ValidationMessageLevel.ERROR,
                    ObjectType.PRODUCTION,
                    taskProductionDto.getName().toString(),
                    taskProductionDto.getId(),
                    taskProductionDto.getRevisionNumber(),
                    "Negative values for production"
            ));
        }
    }
}
