package com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.service.validation;

import com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskMovableDemandDto;
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
public class MovableDemandValidation {

    public void validate(List<TaskMovableDemandDto> taskMovableDemandDtoList, ValidationMessages.ValidationMessagesBuilder builder) {

        checkMovableDemandIds(builder, taskMovableDemandDtoList);
        taskMovableDemandDtoList.forEach(taskMovableDemandDto -> checkMovableDemandValues(builder, taskMovableDemandDto));
    }

    private void checkMovableDemandIds(ValidationMessages.ValidationMessagesBuilder builder, List<TaskMovableDemandDto> taskMovableDemandDtoList) {

        Set<Long> uniqueIds = new HashSet<>(taskMovableDemandDtoList.size());
        for (TaskMovableDemandDto taskMovableDemandDto : taskMovableDemandDtoList) {

            if (!uniqueIds.add(taskMovableDemandDto.getId())) {
                builder.message(new ValidationMessage(
                        ValidationMessageLevel.ERROR,
                        ObjectType.MOVABLE_DEMAND,
                        taskMovableDemandDto.getName().toString(),
                        taskMovableDemandDto.getId(),
                        taskMovableDemandDto.getRevisionNumber(),
                        "Not unique base object id: " + taskMovableDemandDto.getId()));
            }
        }
    }

    private void checkMovableDemandValues(ValidationMessages.ValidationMessagesBuilder builder, TaskMovableDemandDto taskMovableDemandDto) {

        if (taskMovableDemandDto.getMovableDemandValues().stream().anyMatch(value -> value.getValue() < 0.0)) {
            builder.message(new ValidationMessage(
                    ValidationMessageLevel.ERROR,
                    ObjectType.MOVABLE_DEMAND,
                    taskMovableDemandDto.getName().toString(),
                    taskMovableDemandDto.getId(),
                    taskMovableDemandDto.getRevisionNumber(),
                    "Negative values for movable demand profile"
            ));
        }
    }
}
