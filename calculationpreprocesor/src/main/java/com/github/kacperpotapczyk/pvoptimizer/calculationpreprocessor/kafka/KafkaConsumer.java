package com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.kafka;

import com.github.kacperpotapczyk.pvoptimizer.avro.calculationpreprocessor.validation.TaskValidationMessagesDto;
import com.github.kacperpotapczyk.pvoptimizer.avro.postprocessor.TaskPostProcessDataDto;
import com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskCalculationDto;
import com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.mapper.ValidationMessageMapper;
import com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.message.ValidationMessages;
import com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.service.preprocess.PreProcessResult;
import com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.service.preprocess.TaskPreProcess;
import com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.service.validation.ValidationService;
import com.github.kacperpotapczyk.pvoptimizer.avro.optimizer.task.TaskDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaConsumer {

    private final KafkaTemplate<String, TaskValidationMessagesDto> validationKafkaTemplate;
    private final KafkaTemplate<String, TaskDto> taskKafkaTemplate;
    private final KafkaTemplate<String, TaskPostProcessDataDto> postProcessorKafkaTemplate;
    private final ValidationMessageMapper validationMessageMapper;
    private final ValidationService validationService;
    private final TaskPreProcess taskPreProcess;
    @Value("${spring.kafka.producer.validationTopic}")
    private String validationTopic;
    @Value("${spring.kafka.producer.taskTopic}")
    private String taskTopic;
    @Value("${spring.kafka.producer.postprocessorTopic}")
    private String postProcessorTopic;

    @Autowired
    public KafkaConsumer(
            @Qualifier("validation") KafkaTemplate<String, TaskValidationMessagesDto> validationKafkaTemplate,
            @Qualifier("task") KafkaTemplate<String, TaskDto> taskKafkaTemplate, KafkaTemplate<String, TaskPostProcessDataDto> postProcessorKafkaTemplate,
            ValidationMessageMapper validationMessageMapper,
            ValidationService validationService,
            TaskPreProcess taskPreProcess
    ) {
        this.validationKafkaTemplate = validationKafkaTemplate;
        this.taskKafkaTemplate = taskKafkaTemplate;
        this.postProcessorKafkaTemplate = postProcessorKafkaTemplate;
        this.validationMessageMapper = validationMessageMapper;
        this.validationService = validationService;
        this.taskPreProcess = taskPreProcess;
    }

    @KafkaListener(topics = "${spring.kafka.consumer.topic}")
    public void taskListener(@Payload ConsumerRecord<String, TaskCalculationDto> consumerRecord, Acknowledgment acknowledgment) {

        TaskCalculationDto taskCalculationDto = consumerRecord.value();
        ValidationMessages validationMessages = validationService.validate(taskCalculationDto);

        String recordKey = consumerRecord.key();
        log.info("Received record with key: {} from topic {}", recordKey, consumerRecord.topic());

        if (validationMessages.hasErrorMessage()) {
            validationMessages.setValidationSummary("There are validations errors. Task cannot be solved.");
            log.error("Record: {} has validation errors", recordKey);
        }
        else {
            validationMessages.setValidationSummary("Task validation successful.");
            log.debug("Record: {} validation successful", recordKey);
        }

        validationKafkaTemplate.send(
                validationTopic, recordKey,
                validationMessageMapper.mapValidationMessagesToValidationMessagesDto(validationMessages)
        );
        log.info("Validation message for record: {} send to topic: {}", recordKey, validationTopic);

        if (!validationMessages.hasErrorMessage()) {

            PreProcessResult preProcessResult = taskPreProcess.preProcess(taskCalculationDto);

            postProcessorKafkaTemplate.send(
                    postProcessorTopic, recordKey,
                    preProcessResult.taskPostProcessDataDto()
            );
            log.info("Post-process data for record: {} send to topic: {}", recordKey, postProcessorTopic);
            taskKafkaTemplate.send(
                    taskTopic, recordKey,
                    preProcessResult.taskDto()
            );
            log.info("Optimization task with key: {} send to topic: {}", recordKey, taskTopic);
        }
        acknowledgment.acknowledge();
    }
}
