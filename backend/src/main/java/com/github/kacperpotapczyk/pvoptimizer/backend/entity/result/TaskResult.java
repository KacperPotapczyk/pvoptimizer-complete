package com.github.kacperpotapczyk.pvoptimizer.backend.entity.result;

import com.github.kacperpotapczyk.pvoptimizer.backend.entity.task.Task;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "task_result", indexes = {@Index(name = "idx_task_result_task_id_unq", columnList = "task_id", unique = true)})
public class TaskResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne(optional = false, orphanRemoval = true)
    @JoinColumn(name = "task_id", nullable = false, unique = true)
    private Task task;

    @Column(name = "result_status", nullable = false, columnDefinition = "int")
    @Enumerated(EnumType.ORDINAL)
    private ResultStatus resultStatus;

    @OneToMany(mappedBy = "taskResult", cascade = CascadeType.ALL)
    private List<ValidationMessage> validationMessages;

    @Column(name = "objective_function_value")
    private Double objectiveFunctionValue;

    @Column(name = "relative_gap")
    private Double relativeGap;

    @Column(name = "elapsed_time")
    private Double elapsedTime;

    @Column(name = "optimizer_message")
    private String optimizerMessage;

    @OneToMany(mappedBy = "taskResult", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContractResult> contractResults;

    @Column(name = "created_date_time")
    @CreationTimestamp
    private LocalDateTime createdDateTime;

    @Column(name = "update_dateTime")
    @UpdateTimestamp
    private LocalDateTime updateDateTime;

    public TaskResult(Task task) {
        this.id = 0L;
        this.task = task;
        this.resultStatus = ResultStatus.STARTED;
    }

    public void addContractResult(ContractResult contractResult) {

        if (this.contractResults == null) {
            this.contractResults = new ArrayList<>();
        }

        this.contractResults.add(contractResult);
        contractResult.setTaskResult(this);
    }
}