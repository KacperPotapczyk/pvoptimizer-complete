package com.github.kacperpotapczyk.pvoptimizer.backend.entity.contract;

import com.github.kacperpotapczyk.pvoptimizer.backend.entity.BaseObject;
import com.github.kacperpotapczyk.pvoptimizer.backend.entity.tariff.Tariff;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Table(name = "contract", uniqueConstraints = {@UniqueConstraint(name = "uc_contract_name_is_deleted", columnNames = {"name", "is_deleted"})})
public class Contract extends BaseObject<ContractRevision> {

    @Column(name = "contract_type", nullable = false, columnDefinition = "int")
    @Enumerated(EnumType.ORDINAL)
    private ContractType contractType;

    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ContractRevision> revisions;

    @ManyToOne
    @JoinColumn(name = "tariff_base_id")
    private Tariff tariff;

    public Set<ContractRevision> getRevisions() {
        return revisions.stream()
                .filter(contractRevision -> !contractRevision.isDeleted())
                .collect(Collectors.toSet());
    }

    public void addRevision(ContractRevision contractRevision) {

        if (this.revisions == null) {
            this.revisions = new HashSet<>();
        }

        contractRevision.setContract(this);
        this.revisions.add(contractRevision);
    }

    public Optional<ContractRevision> getRevision(long revisionNumber) {

        return revisions.stream()
                .filter(revision -> revision.getRevisionNumber() == revisionNumber)
                .findFirst();
    }

    public Optional<Long> getLastRevisionNumber() {

        return revisions.stream()
                .map(ContractRevision::getRevisionNumber)
                .max(Long::compareTo);
    }

    @Override
    public void softDelete() {
        setDeleted(true);
        revisions.forEach(ContractRevision::softDelete);
    }
}