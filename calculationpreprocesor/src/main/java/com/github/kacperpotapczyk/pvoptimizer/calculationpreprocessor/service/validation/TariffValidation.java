package com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.service.validation;

import com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskTariffDto;
import com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.message.ObjectType;
import com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.message.ValidationMessage;
import com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.message.ValidationMessageLevel;
import com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.message.ValidationMessages;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class TariffValidation {

    public void validate(List<TaskTariffDto> taskTariffDtoList, ValidationMessages.ValidationMessagesBuilder builder) {

        checkTariffIds(builder, taskTariffDtoList);
    }

    // TODO check tariff name uniqueness. Contracts find their tariff by name
    private void checkTariffIds(ValidationMessages.ValidationMessagesBuilder builder, List<TaskTariffDto> taskTariffDtoList) {

        Set<Long> uniqueIds = new HashSet<>(taskTariffDtoList.size());
        for (TaskTariffDto taskTariffDto : taskTariffDtoList) {

            if (!uniqueIds.add(taskTariffDto.getId())) {
                builder.message(new ValidationMessage(
                        ValidationMessageLevel.ERROR,
                        ObjectType.TARIFF,
                        taskTariffDto.getName().toString(),
                        taskTariffDto.getId(),
                        taskTariffDto.getRevisionNumber(),
                        "Not unique base object id: " + taskTariffDto.getId()));
            }
        }
    }
}
