package com.github.kacperpotapczyk.pvoptimizer.backend.kafka;

import com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskCalculationDto;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
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
public class KafkaProducerConfig {

    private final KafkaProperties kafkaProperties;

    @Autowired
    public KafkaProducerConfig(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    @Bean
    public KafkaTemplate<String, TaskCalculationDto> kafkaTemplate(final ProducerFactory<String, TaskCalculationDto> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public ProducerFactory<String, TaskCalculationDto> producerFactory(SslBundles sslBundles) {
        return new DefaultKafkaProducerFactory<>(
                kafkaProperties.buildProducerProperties(sslBundles),
                StringSerializer::new,
                SpecificAvroSerializer<TaskCalculationDto>::new
        );
    }
}
