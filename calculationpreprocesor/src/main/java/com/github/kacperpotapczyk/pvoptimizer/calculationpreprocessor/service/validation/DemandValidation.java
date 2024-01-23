package com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.service.validation;

import com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskDemandDto;
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
public class DemandValidation {

    public void validate(List<TaskDemandDto> taskDemandDtoList, ValidationMessages.ValidationMessagesBuilder builder) {

        checkDemandIds(builder, taskDemandDtoList);
        taskDemandDtoList.forEach(taskDemandDto -> checkDemandValues(builder, taskDemandDto));
    }

    private void checkDemandIds(ValidationMessages.ValidationMessagesBuilder builder, List<TaskDemandDto> taskDemandDtoList) {

        Set<Long> uniqueIds = new HashSet<>(taskDemandDtoList.size());
        for (TaskDemandDto taskDemandDto : taskDemandDtoList) {

            if (!uniqueIds.add(taskDemandDto.getId())) {
                builder.message(new ValidationMessage(
                        ValidationMessageLevel.ERROR,
                        ObjectType.DEMAND,
                        taskDemandDto.getName().toString(),
                        taskDemandDto.getId(),
                        taskDemandDto.getRevisionNumber(),
                        "Not unique base object id: " + taskDemandDto.getId()));
            }
        }
    }

    private void checkDemandValues(ValidationMessages.ValidationMessagesBuilder builder, TaskDemandDto taskDemandDto) {

        if (taskDemandDto.getDemandValues().stream().anyMatch(value -> value.getValue() < 0.0)) {
            builder.message(new ValidationMessage(
                    ValidationMessageLevel.ERROR,
                    ObjectType.DEMAND,
                    taskDemandDto.getName().toString(),
                    taskDemandDto.getId(),
                    taskDemandDto.getRevisionNumber(),
                    "Negative values for demand"
            ));
        }
    }
}
