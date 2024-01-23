package com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.mapper;

import com.github.kacperpotapczyk.pvoptimizer.avro.calculationpreprocessor.validation.TaskValidationMessageDto;
import com.github.kacperpotapczyk.pvoptimizer.avro.calculationpreprocessor.validation.TaskValidationMessagesDto;
import com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.message.ValidationMessage;
import com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.message.ValidationMessages;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ValidationMessageMapper {

    TaskValidationMessagesDto mapValidationMessagesToValidationMessagesDto(ValidationMessages validationMessages);

    TaskValidationMessageDto mapValidationMessageToValidationMessageDto(ValidationMessage validationMessage);
}
