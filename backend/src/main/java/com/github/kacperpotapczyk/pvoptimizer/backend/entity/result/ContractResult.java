package com.github.kacperpotapczyk.pvoptimizer.backend.entity.result;

import com.github.kacperpotapczyk.pvoptimizer.backend.entity.contract.ContractRevision;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "contract_result", indexes = {@Index(name = "idx_contract_result_task_result_id", columnList = "task_result_id")})
public class ContractResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "contract_revision_id", nullable = false)
    private ContractRevision contractRevision;

    @ManyToOne(optional = false)
    @JoinColumn(name = "task_result_id", nullable = false)
    private TaskResult taskResult;

    @OneToMany(mappedBy = "contractResult", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContractResultValue> contractResultValues;
}