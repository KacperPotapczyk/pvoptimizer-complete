package com.github.kacperpotapczyk.pvoptimizer.backend.kafka;

import com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.result.TaskCalculationResultDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.service.TaskResultService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ResultConsumer {

    private final TaskResultService taskResultService;

    @Autowired
    public ResultConsumer(TaskResultService taskResultService) {
        this.taskResultService = taskResultService;
    }

    @KafkaListener(topics = "${spring.kafka.consumer.resultTopic}", containerFactory = "taskResultKafkaListenerContainerFactory")
    public void resultListener(@Payload ConsumerRecord<String, TaskCalculationResultDto> consumerRecord, Acknowledgment acknowledgment) {

        log.info("Received optimization result with key: {} from topic: {}", consumerRecord.key(), consumerRecord.topic());
        taskResultService.addCalculationResult(consumerRecord.value());
        acknowledgment.acknowledge();
    }
}
