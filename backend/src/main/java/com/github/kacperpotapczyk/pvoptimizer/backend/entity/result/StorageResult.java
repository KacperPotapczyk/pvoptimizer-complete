package com.github.kacperpotapczyk.pvoptimizer.backend.entity.result;

import com.github.kacperpotapczyk.pvoptimizer.backend.entity.storage.StorageRevision;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "storage_result", indexes = {@Index(name = "idx_storage_result_task_result_id", columnList = "task_result_id")})
public class StorageResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "storage_revision_id", nullable = false)
    private StorageRevision storageRevision;

    @ManyToOne(optional = false)
    @JoinColumn(name = "task_result_id", nullable = false)
    private TaskResult taskResult;

    @OneToMany(mappedBy = "storageResult", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StorageResultValue> storageResultValues;
}