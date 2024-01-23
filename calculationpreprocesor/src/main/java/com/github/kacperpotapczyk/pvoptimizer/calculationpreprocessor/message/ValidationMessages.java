package com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.message;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;

import java.util.List;

@Getter
@Builder
public class ValidationMessages {

    private final long taskId;
    @Singular
    private final List<ValidationMessage> messages;
    @Setter
    private String validationSummary;

    public boolean hasErrorMessage() {

        return messages.stream()
                .anyMatch(validationMessage -> validationMessage.level() == ValidationMessageLevel.ERROR);
    }
}
