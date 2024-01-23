package com.github.kacperpotapczyk.pvoptimizer.backend.mapper;

import com.github.kacperpotapczyk.pvoptimizer.backend.entity.result.ValidationMessage;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.result.ValidationMessageLevel;
import com.github.kacperpotapczyk.pvoptimizer.avro.calculationpreprocessor.validation.TaskValidationMessageDto;
import com.github.kacperpotapczyk.pvoptimizer.avro.calculationpreprocessor.validation.TaskValidationMessageLevelDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ValueMapping;

@Mapper(componentModel = "spring")
public interface ValidationMessagesMapper {

    @Mapping(target = "taskResult", ignore = true)
    @Mapping(target = "objectId", source = "id")
    @Mapping(target = "objectRevision", source = "revisionId")
    @Mapping(target = "objectName", source = "baseObjectName")
    ValidationMessage mapValidationMessageDtoToValidationMessage(TaskValidationMessageDto validationMessageDto);

    @ValueMapping(target = "WARNING", source = "WARN")
    ValidationMessageLevel mapValidationMessageLevelDtoToValidationMessageLevel(TaskValidationMessageLevelDto validationMessageLevelDto);

    default String map(CharSequence charSequence) {
        return charSequence.toString();
    }
}
