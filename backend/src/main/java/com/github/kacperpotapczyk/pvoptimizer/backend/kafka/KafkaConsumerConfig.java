package com.github.kacperpotapczyk.pvoptimizer.backend.kafka;

import com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.result.TaskCalculationResultDto;
import com.github.kacperpotapczyk.pvoptimizer.avro.calculationpreprocessor.validation.TaskValidationMessagesDto;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroDeserializer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@EnableKafka
@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {

    private final KafkaProperties kafkaProperties;
    @Value("${spring.kafka.consumer.backoff.interval}")
    private long interval;
    @Value("${spring.kafka.consumer.backoff.maxAttempts}")
    private long maxAttempts;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TaskValidationMessagesDto> validationKafkaListenerContainerFactory(SslBundles sslBundles) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, TaskValidationMessagesDto>();
        factory.setConsumerFactory(validationConsumerFactory(sslBundles));
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.setCommonErrorHandler(new DefaultErrorHandler(new FixedBackOff(interval, maxAttempts)));

        return factory;
    }

    @Bean
    public ConsumerFactory<String, TaskValidationMessagesDto> validationConsumerFactory(SslBundles sslBundles) {
        return new DefaultKafkaConsumerFactory<>(kafkaProperties.buildConsumerProperties(sslBundles), StringDeserializer::new, SpecificAvroDeserializer<TaskValidationMessagesDto>::new);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TaskCalculationResultDto> taskResultKafkaListenerContainerFactory(SslBundles sslBundles) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, TaskCalculationResultDto>();
        factory.setConsumerFactory(taskResultConsumerFactory(sslBundles));
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.setCommonErrorHandler(new DefaultErrorHandler(new FixedBackOff(interval, maxAttempts)));

        return factory;
    }

    @Bean
    public ConsumerFactory<String, TaskCalculationResultDto> taskResultConsumerFactory(SslBundles sslBundles) {
        return new DefaultKafkaConsumerFactory<>(kafkaProperties.buildConsumerProperties(sslBundles), StringDeserializer::new, SpecificAvroDeserializer<TaskCalculationResultDto>::new);
    }
}
