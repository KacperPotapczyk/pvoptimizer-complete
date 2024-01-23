package com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.message;

public record ValidationMessage(
        ValidationMessageLevel level,
        ObjectType objectType,
        String baseObjectName,
        long id,
        long revisionId,
        String message
) {
    public ValidationMessage withMessage(String message) {

        return new ValidationMessage(
                this.level,
                this.objectType,
                this.baseObjectName,
                this.id,
                this.revisionId,
                message
        );
    }

    public static ValidationMessage messageTemplate(ValidationMessageLevel validationMessageLevel, ObjectType objectType, String baseObjectName, long id, long revisionId) {

        return new ValidationMessage(
                validationMessageLevel,
                objectType,
                baseObjectName,
                id,
                revisionId,
                "Message template."
        );
    }

    public static ValidationMessage debugMessageTemplate(ObjectType objectType, String baseObjectName, long id, long revisionId) {

        return ValidationMessage.messageTemplate(
                ValidationMessageLevel.DEBUG,
                objectType,
                baseObjectName,
                id,
                revisionId
        );
    }

    public static ValidationMessage infoMessageTemplate(ObjectType objectType, String baseObjectName, long id, long revisionId) {

        return ValidationMessage.messageTemplate(
                ValidationMessageLevel.INFO,
                objectType,
                baseObjectName,
                id,
                revisionId
        );
    }

    public static ValidationMessage warnMessageTemplate(ObjectType objectType, String baseObjectName, long id, long revisionId) {

        return ValidationMessage.messageTemplate(
                ValidationMessageLevel.WARN,
                objectType,
                baseObjectName,
                id,
                revisionId
        );
    }

    public static ValidationMessage errorMessageTemplate(ObjectType objectType, String baseObjectName, long id, long revisionId) {

        return  ValidationMessage.messageTemplate(
                ValidationMessageLevel.ERROR,
                objectType,
                baseObjectName,
                id,
                revisionId
        );
    }
}
