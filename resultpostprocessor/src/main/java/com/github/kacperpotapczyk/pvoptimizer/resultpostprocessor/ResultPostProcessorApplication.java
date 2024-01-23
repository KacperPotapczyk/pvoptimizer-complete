package com.github.kacperpotapczyk.pvoptimizer.resultpostprocessor;

import com.github.kacperpotapczyk.pvoptimizer.avro.postprocessor.TaskPostProcessDataDto;
import com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.result.*;
import com.github.kacperpotapczyk.pvoptimizer.avro.optimizer.result.ResultDto;
import com.github.kacperpotapczyk.pvoptimizer.resultpostprocessor.service.ResultPostProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Duration;
import java.util.function.BiFunction;
@Slf4j
@SpringBootApplication
public class ResultPostProcessorApplication {

	private final ResultPostProcessor resultPostProcessor;

	@Autowired
    public ResultPostProcessorApplication(ResultPostProcessor resultPostProcessor) {
        this.resultPostProcessor = resultPostProcessor;
    }


    public static void main(String[] args) {
		SpringApplication.run(ResultPostProcessorApplication.class, args);
	}

	@Bean
	public BiFunction<KStream<String, TaskPostProcessDataDto>, KStream<String, ResultDto>, KStream<String, TaskCalculationResultDto>> process()  {
		return (taskCalculationDtoKStream, resultDtoKStream) -> taskCalculationDtoKStream
				.join(resultDtoKStream, resultPostProcessor::postProcess, JoinWindows.ofTimeDifferenceWithNoGrace(Duration.ofSeconds(60)))
				.peek((k, v) -> log.info("Joined task and result with key: {}", k));
	}

}
