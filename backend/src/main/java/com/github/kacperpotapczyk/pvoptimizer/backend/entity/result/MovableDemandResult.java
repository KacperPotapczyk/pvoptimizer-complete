package com.github.kacperpotapczyk.pvoptimizer.backend.entity.result;

import com.github.kacperpotapczyk.pvoptimizer.backend.entity.movabledemand.MovableDemandRevision;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "movable_demand_result", indexes = {@Index(name = "idx_movable_demand_result_task_result_id", columnList = "task_result_id")})
public class MovableDemandResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "movable_demand_revision_id", nullable = false)
    private MovableDemandRevision movableDemandRevision;

    @ManyToOne(optional = false)
    @JoinColumn(name = "task_result_id", nullable = false)
    private TaskResult taskResult;

    @OneToMany(mappedBy = "movableDemandResult", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MovableDemandResultValue> movableDemandResultValues;
}