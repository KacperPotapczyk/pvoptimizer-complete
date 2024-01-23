package com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.kafka;

import com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskCalculationDto;
import com.github.kacperpotapczyk.pvoptimizer.avro.calculationpreprocessor.validation.TaskValidationMessagesDto;
import com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.mapper.ValidationMessageMapper;
import com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.message.ValidationMessages;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroDeserializer;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@EnableKafka
@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {

    private final KafkaProperties kafkaProperties;
    private final KafkaTemplate<String, TaskValidationMessagesDto> validationKafkaTemplate;
    private final ValidationMessageMapper validationMessageMapper;
    @Value("${spring.kafka.producer.validationTopic}")
    private String validationTopic;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TaskCalculationDto> kafkaListenerContainerFactory(SslBundles sslBundles) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, TaskCalculationDto>();
        factory.setConsumerFactory(consumerFactory(sslBundles));
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);

        factory.setCommonErrorHandler(new DefaultErrorHandler(
                ((consumerRecord, e) -> {
                    String key = consumerRecord.key().toString();
                    ValidationMessages.ValidationMessagesBuilder builder = ValidationMessages.builder();
                    builder.taskId(0);
                    builder.validationSummary("Cannot process message with key: " + key);

                    validationKafkaTemplate.send(
                            validationTopic,
                            key,
                            validationMessageMapper.mapValidationMessagesToValidationMessagesDto(builder.build())
                    );
                }),
                new FixedBackOff(10_000L, 5L)
        ));

        return factory;
    }

    @Bean
    public ConsumerFactory<String, TaskCalculationDto> consumerFactory(SslBundles sslBundles) {
        return new DefaultKafkaConsumerFactory<>(kafkaProperties.buildConsumerProperties(sslBundles), StringDeserializer::new, SpecificAvroDeserializer<TaskCalculationDto>::new);
    }
}
