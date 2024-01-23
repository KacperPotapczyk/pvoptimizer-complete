package com.github.kacperpotapczyk.pvoptimizer.backend.entity.task;

import com.github.kacperpotapczyk.pvoptimizer.backend.entity.contract.ContractRevision;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.demand.DemandRevision;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.tariff.TariffRevision;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "task", uniqueConstraints = {@UniqueConstraint(name = "uc_task_name", columnNames = {"name"})})
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "date_time_start", nullable = false)
    private LocalDateTime dateTimeStart;

    @Column(name = "date_time_end", nullable = false)
    private LocalDateTime dateTimeEnd;

    @Column(name = "read_only", nullable = false, columnDefinition = "boolean default false")
    private boolean readOnly;

    @Column(name = "created_date_time")
    @CreationTimestamp
    private LocalDateTime createdDateTime;

    @Column(name = "update_dateTime")
    @UpdateTimestamp
    private LocalDateTime updateDateTime;

    @ManyToMany
    @JoinTable(name = "task_demand_revisions", joinColumns = @JoinColumn(name = "task_id"), inverseJoinColumns = @JoinColumn(name = "revision_id"))
    private Set<DemandRevision> demandRevisions;

    @ManyToMany
    @JoinTable(name = "task_tariff_revisions", joinColumns = @JoinColumn(name = "task_id"), inverseJoinColumns = @JoinColumn(name = "revision_id"))
    private Set<TariffRevision> tariffRevisions;

    @ManyToMany
    @JoinTable(name = "task_contract_revisions", joinColumns = @JoinColumn(name = "task_id"), inverseJoinColumns = @JoinColumn(name = "revision_id"))
    private Set<ContractRevision> contractRevisions;

    public Task(String name, LocalDateTime dateTimeStart, LocalDateTime dateTimeEnd) {
        this.name = name;
        this.dateTimeStart = dateTimeStart;
        this.dateTimeEnd = dateTimeEnd;
    }
    public void addDemandRevision(DemandRevision demandRevision) {

        if (this.demandRevisions == null) {
            this.demandRevisions = new HashSet<>();
        }
        this.demandRevisions.add(demandRevision);
    }

    public void addTariffRevision(TariffRevision tariffRevision) {

        if (this.tariffRevisions == null) {
            this.tariffRevisions = new HashSet<>();
        }
        this.tariffRevisions.add(tariffRevision);
    }

    public void addContractRevision(ContractRevision contractRevision) {

        if (this.contractRevisions == null) {
            this.contractRevisions = new HashSet<>();
        }
        this.contractRevisions.add(contractRevision);
    }
}