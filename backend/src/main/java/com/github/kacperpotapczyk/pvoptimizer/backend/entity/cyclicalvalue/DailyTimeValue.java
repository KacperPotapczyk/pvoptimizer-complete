package com.github.kacperpotapczyk.pvoptimizer.backend.entity.cyclicalvalue;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "daily_time_value", indexes = {@Index(name = "idx_daily_time_value_daily_value_id", columnList = "daily_value_id")})
public class DailyTimeValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "daily_value_id", nullable = false)
    private CyclicalDailyValue cyclicalDailyValues;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "current_value", nullable = false)
    private double currentValue;
}