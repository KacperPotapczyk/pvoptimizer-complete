package com.github.kacperpotapczyk.pvoptimizer.backend.kafka;

import com.github.kacperpotapczyk.pvoptimizer.avro.calculationpreprocessor.validation.TaskValidationMessagesDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.result.ValidationMessage;
import com.github.kacperpotapczyk.pvoptimizer.backend.mapper.ValidationMessagesMapper;
import com.github.kacperpotapczyk.pvoptimizer.backend.service.TaskResultService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ValidationConsumer {

    private final TaskResultService taskResultService;
    private final ValidationMessagesMapper mapper;

    @Autowired
    public ValidationConsumer(TaskResultService taskResultService, ValidationMessagesMapper mapper) {
        this.taskResultService = taskResultService;
        this.mapper = mapper;
    }

    @KafkaListener(topics = "${spring.kafka.consumer.validationTopic}", containerFactory = "validationKafkaListenerContainerFactory")
    public void validationListener(@Payload ConsumerRecord<String, TaskValidationMessagesDto> consumerRecord, Acknowledgment acknowledgment) {

        log.info("Received validation message with key: {} from topic: {}", consumerRecord.key(), consumerRecord.topic());

        TaskValidationMessagesDto validationMessagesDto = consumerRecord.value();
        List<ValidationMessage> validationMessages = validationMessagesDto.getMessages().stream()
                .map(mapper::mapValidationMessageDtoToValidationMessage)
                .collect(Collectors.toList());

        taskResultService.addValidationResult(validationMessagesDto.getTaskId(), validationMessages);

        acknowledgment.acknowledge();
    }
}
