package com.github.kacperpotapczyk.pvoptimizer.backend.service;

import com.github.kacperpotapczyk.pvoptimizer.avro.backend.calculation.task.TaskCalculationDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.task.Task;
import com.github.kacperpotapczyk.pvoptimizer.backend.mapper.TaskCalculationMapper;
import com.github.kacperpotapczyk.pvoptimizer.backend.repository.TaskRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
public class TaskServiceImp implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskResultService taskResultService;
    private final TaskCalculationMapper taskCalculationMapper;
    private final KafkaTemplate<String, TaskCalculationDto> kafkaTemplate;
    @Value("${spring.kafka.producer.topic}")
    private String taskCalculationTopic;

    @Autowired
    public TaskServiceImp(TaskRepository taskRepository, TaskResultService taskResultService, TaskCalculationMapper taskCalculationMapper, KafkaTemplate<String, TaskCalculationDto> kafkaTemplate) {
        this.taskRepository = taskRepository;
        this.taskResultService = taskResultService;
        this.taskCalculationMapper = taskCalculationMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public boolean existsById(Long id) {
        return taskRepository.existsById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return taskRepository.existsByName(name);
    }

    @Override
    public Task findByName(String name) {

        return taskRepository.findByName(name)
                .orElseThrow(() -> new ObjectNotFoundException(Task.class, name));
    }

    @Override
    @Transactional
    public Task newTask(Task task) {

        if (!taskRepository.existsByName(task.getName())) {

            task.setId(0L);
            return taskRepository.save(task);
        }
        else {
            throw new IllegalArgumentException("Task with given name: " + task.getName() + " already exists in DB");
        }
    }

    @Override
    @Transactional
    public Task updateTask(Task task) {

        if (taskRepository.existsByNameAndReadOnly(task.getName(), false)) {
            task.setId(taskRepository.findIdByName(task.getName()));
            return taskRepository.save(task);
        }
        else {
            if (taskRepository.existsByName(task.getName())) {
                throw new IllegalArgumentException("Task with given name: " + task.getName() + " is read only");
            }
            else {
                throw new IllegalArgumentException("Task with given name: " + task.getName() + " does not exists in DB");
            }
        }
    }

    @Override
    @Transactional
    public void deleteTask(String name) {

        taskRepository.findByName(name).ifPresentOrElse(
                t -> taskRepository.deleteById(t.getId()),
                () -> {throw new ObjectNotFoundException(Task.class, name);}
        );
    }

    @Override
    public Page<Task> getTasks(Pageable pageable) {

        return taskRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public void sendTaskForComputations(String name) {

        taskRepository.findByNameAndReadOnly(name, false).ifPresentOrElse(
                this::sendTaskForCalculationAndMarkAsReadOnly,
                () -> {throw new ObjectNotFoundException(Task.class, name);}
        );
    }

    private void sendTaskForCalculationAndMarkAsReadOnly(Task task) {

        String messageKey = UUID.randomUUID().toString();
        try {
            task.setReadOnly(true);
            taskRepository.save(task);
            taskResultService.initiateResult(task);

            log.info("Sending task with id: {} in message with key: {} to topic: {}", task.getId(), messageKey, taskCalculationTopic);
            kafkaTemplate
                    .send(taskCalculationTopic, messageKey, taskCalculationMapper.mapTaskToTaskCalculationDto(task))
                    .get(30L, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            log.error("Could not send task with id: {} in message with key: {} to topic: {}", task.getId(), messageKey, taskCalculationTopic);
            throw new RuntimeException("Error while sending task calculation to kafka topic.", ex);
        }
    }
}
