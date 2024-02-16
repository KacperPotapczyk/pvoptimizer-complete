package com.github.kacperpotapczyk.pvoptimizer.backend.entity.result;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "storage_result_value", indexes = {@Index(name = "idx_storage_result_value_storage_result_id", columnList = "storage_result_id")})
public class StorageResultValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "storage_result_id")
    private StorageResult storageResult;

    @Column(name = "date_time_start", nullable = false)
    private LocalDateTime dateTimeStart;

    @Column(name = "date_time_end", nullable = false)
    private LocalDateTime dateTimeEnd;

    @Column(name = "charge", nullable = false)
    private double charge;

    @Column(name = "discharge", nullable = false)
    private double discharge;

    @Column(name = "energy", nullable = false)
    private double energy;

    @Column(name = "storage_mode", nullable = false, columnDefinition = "int")
    private StorageMode storageMode;
}