package com.github.kacperpotapczyk.pvoptimizer.backend.entity.storage;

import com.github.kacperpotapczyk.pvoptimizer.backend.entity.BaseObject;
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
@Table(name = "storage", uniqueConstraints = {@UniqueConstraint(name = "uc_storage_name_is_deleted", columnNames = {"name", "is_deleted"})})
public class Storage extends BaseObject<StorageRevision> {

    @OneToMany(mappedBy = "storage", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<StorageRevision> revisions;

    @Column(name = "capacity", nullable = false)
    private double capacity;

    @Column(name = "max_charge", nullable = false)
    private double maxCharge;

    @Column(name = "max_discharge", nullable = false)
    private double maxDischarge;

    @Override
    public Set<StorageRevision> getRevisions() {

        return revisions.stream()
                .filter(storageRevision -> !storageRevision.isDeleted())
                .collect(Collectors.toSet());
    }

    @Override
    public void addRevision(StorageRevision storageRevision) {

        if (this.revisions == null) {
            this.revisions = new HashSet<>();
        }

        storageRevision.setStorage(this);
        this.revisions.add(storageRevision);
    }

    @Override
    public Optional<StorageRevision> getRevision(long revisionNumber) {

        return revisions.stream()
                .filter(storageRevision -> storageRevision.getRevisionNumber() == revisionNumber)
                .findFirst();
    }

    @Override
    public Optional<Long> getLastRevisionNumber() {

        return revisions.stream()
                .map(StorageRevision::getRevisionNumber)
                .max(Long::compareTo);
    }

    @Override
    public void softDelete() {

        revisions.forEach(StorageRevision::softDelete);
        setDeleted(true);
    }
}