package com.github.kacperpotapczyk.pvoptimizer.calculationpreprocessor.kafka;

import com.github.kacperpotapczyk.pvoptimizer.avro.postprocessor.TaskPostProcessDataDto;
import com.github.kacperpotapczyk.pvoptimizer.avro.calculationpreprocessor.validation.TaskValidationMessagesDto;
import com.github.kacperpotapczyk.pvoptimizer.avro.optimizer.task.TaskDto;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerializer;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@EnableKafka
@Configuration
@RequiredArgsConstructor
public class KafkaProducerConfig {

    private final KafkaProperties kafkaProperties;

    @Bean(name = "validation")
    public KafkaTemplate<String, TaskValidationMessagesDto> validationKafkaTemplate(final ProducerFactory<String, TaskValidationMessagesDto> validationProducerFactory) {
        return new KafkaTemplate<>(validationProducerFactory);
    }

    @Bean
    public ProducerFactory<String, TaskValidationMessagesDto> validationProducerFactory(SslBundles sslBundles) {
        return new DefaultKafkaProducerFactory<>(kafkaProperties.buildConsumerProperties(sslBundles), StringSerializer::new, SpecificAvroSerializer<TaskValidationMessagesDto>::new);
    }

    @Bean(name = "task")
    public KafkaTemplate<String, TaskDto> taskKafkaTemplate(final ProducerFactory<String, TaskDto> taskProducerFactory) {
        return new KafkaTemplate<>(taskProducerFactory);
    }

    @Bean
    public ProducerFactory<String, TaskDto> taskProducerFactory(SslBundles sslBundles) {
        return new DefaultKafkaProducerFactory<>(kafkaProperties.buildConsumerProperties(sslBundles), StringSerializer::new, SpecificAvroSerializer<TaskDto>::new);
    }

    @Bean(name = "postProcessor")
    public KafkaTemplate<String, TaskPostProcessDataDto> postProcessorKafkaTemplate(final ProducerFactory<String, TaskPostProcessDataDto> taskProducerFactory) {
        return new KafkaTemplate<>(taskProducerFactory);
    }

    @Bean
    public ProducerFactory<String, TaskPostProcessDataDto> postProcessorProducerFactory(SslBundles sslBundles) {
        return new DefaultKafkaProducerFactory<>(kafkaProperties.buildConsumerProperties(sslBundles), StringSerializer::new, SpecificAvroSerializer<TaskPostProcessDataDto>::new);
    }
}
