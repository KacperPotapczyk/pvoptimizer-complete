package com.github.kacperpotapczyk.pvoptimizer.backend.service;

import com.github.kacperpotapczyk.pvoptimizer.backend.entity.task.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {

    boolean existsById(Long id);

    boolean existsByName(String name);

    Task findByName(String name);

    Task newTask(Task task);

    Task updateTask(Task task);

    void deleteTask(String name);

    Page<Task> getTasks(Pageable pageable);

    void sendTaskForComputations(String name);
}
