package com.github.kacperpotapczyk.pvoptimizer.backend.controller;

import com.github.kacperpotapczyk.pvoptimizer.backend.dto.result.TaskResultDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.dto.task.TaskDto;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.task.Task;
import com.github.kacperpotapczyk.pvoptimizer.backend.mapper.TaskMapper;
import com.github.kacperpotapczyk.pvoptimizer.backend.mapper.TaskResultMapper;
import com.github.kacperpotapczyk.pvoptimizer.backend.service.TaskResultService;
import com.github.kacperpotapczyk.pvoptimizer.backend.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final TaskResultService resultService;
    private final TaskMapper taskMapper;
    private final TaskResultMapper resultMapper;

    @Autowired
    public TaskController(TaskService taskService, TaskResultService resultService, TaskMapper taskMapper, TaskResultMapper resultMapper) {
        this.taskService = taskService;
        this.resultService = resultService;
        this.taskMapper = taskMapper;
        this.resultMapper = resultMapper;
    }

    @GetMapping("/list")
    @Operation(summary = "Returns list of all Tasks")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = TaskDto.class))})
    })
    public Page<TaskDto> getTaskList(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "25") int pageSize
    ) {

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("id").ascending());
        Page<Task> tasks = taskService.getTasks(pageable);
        return tasks.map(taskMapper::mapTaskToTaskDto);
    }

    @GetMapping("/{name}")
    @Operation(summary = "Returns task by its name")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Task found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = TaskDto.class))}
            ),
            @ApiResponse(responseCode = "404", description = "Task not found", content = @Content)
    })
    public TaskDto getTask(@PathVariable String name) {

        try {
            return taskMapper.mapTaskToTaskDto(taskService.findByName(name));
        } catch (ObjectNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @PostMapping("")
    @Operation(summary = "Creates new tasks")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Task created",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = TaskDto.class))}
            ),
            @ApiResponse(responseCode = "400", description = "Task could not be created", content = @Content),
            @ApiResponse(responseCode = "404", description = "Task relates to not existing objects", content = @Content)
    })
    public TaskDto addTask(@RequestBody TaskDto taskDto) {

        try {
            return taskMapper.mapTaskToTaskDto(taskService.newTask(taskMapper.mapTaskDtoToTask(taskDto)));
        } catch (ObjectNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

    @PutMapping("")
    @Operation(summary = "Updates existing tasks")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Task updated",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = TaskDto.class))}
            ),
            @ApiResponse(responseCode = "400", description = "Task could not be updated", content = @Content),
            @ApiResponse(responseCode = "404", description = "Task to be updated not be found", content = @Content)
    })
    public TaskDto updateTask(@RequestBody TaskDto taskDto) {

        try {
            return taskMapper.mapTaskToTaskDto(taskService.updateTask(taskMapper.mapTaskDtoToTask(taskDto)));
        } catch (ObjectNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        }
    }

    @DeleteMapping("/{name}")
    @Operation(summary = "Deletes task")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Task deleted",
                    content = @Content
            ),
            @ApiResponse(responseCode = "404", description = "Task not found", content = @Content)
    })
    public void deleteTask(@PathVariable String name) {

        try {
            taskService.deleteTask(name);
        } catch (ObjectNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @PostMapping("/{name}/calculation")
    @Operation(summary = "Starts task calculation")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Task calculation started",
                    content = @Content
            ),
            @ApiResponse(responseCode = "404", description = "Task to be updated not be found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Task could not be send for calculation", content = @Content)
    })
    public void sendTaskForCalculation(@PathVariable String name) {

        try {
            taskService.sendTaskForComputations(name);
        } catch (ObjectNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        } catch (RuntimeException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        }
    }

    @GetMapping("/{name}/result")
    @Operation(summary = "Returns task result by task name")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Task result found",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = TaskResultDto.class))}
            ),
            @ApiResponse(responseCode = "404", description = "Task result not found", content = @Content)
    })
    public TaskResultDto getTaskResult(@PathVariable String name) {

        try {
            return resultMapper.mapTaskResultToTaskResultDto(resultService.getResultForTaskName(name));
        } catch (ObjectNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }
}
