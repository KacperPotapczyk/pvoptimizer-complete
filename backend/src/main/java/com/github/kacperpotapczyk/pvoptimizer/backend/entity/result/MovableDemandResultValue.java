package com.github.kacperpotapczyk.pvoptimizer.backend.entity.result;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "movable_demand_result_value", indexes = {@Index(name = "idx_movable_demand_result_value_movable_demand_result_id", columnList = "movable_demand_result_id")})
public class MovableDemandResultValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "movable_demand_result_id")
    private MovableDemandResult movableDemandResult;

    @Column(name = "date_time_start", nullable = false)
    private LocalDateTime dateTimeStart;

    @Column(name = "date_time_end", nullable = false)
    private LocalDateTime dateTimeEnd;

    @Column(name = "power", nullable = false)
    private double power;

    @Column(name = "energy", nullable = false)
    private double energy;
}