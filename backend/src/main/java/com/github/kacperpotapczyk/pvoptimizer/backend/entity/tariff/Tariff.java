package com.github.kacperpotapczyk.pvoptimizer.backend.entity.tariff;

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
@Table(name = "tariff", uniqueConstraints = {@UniqueConstraint(name = "uc_tariff_name_is_deleted", columnNames = {"name", "is_deleted"})})
public class Tariff extends BaseObject<TariffRevision> {

    @OneToMany(mappedBy = "tariff", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TariffRevision> revisions;

    @Override
    public Set<TariffRevision> getRevisions() {
        return revisions.stream()
                .filter(tariffRevision -> !tariffRevision.isDeleted())
                .collect(Collectors.toSet());
    }

    @Override
    public void addRevision(TariffRevision tariffRevision) {
        if (this.revisions == null) {
            this.revisions = new HashSet<>();
        }

        tariffRevision.setTariff(this);
        this.revisions.add(tariffRevision);
    }

    @Override
    public Optional<TariffRevision> getRevision(long revisionNumber) {
        return this.revisions.stream()
                .filter(tariffRevision -> tariffRevision.getRevisionNumber() == revisionNumber)
                .findFirst();
    }

    @Override
    public Optional<Long> getLastRevisionNumber() {
        return revisions.stream()
                .map(TariffRevision::getRevisionNumber)
                .max(Long::compareTo);
    }

    @Override
    public void softDelete() {
        setDeleted(true);
        revisions.forEach(TariffRevision::softDelete);
    }
}