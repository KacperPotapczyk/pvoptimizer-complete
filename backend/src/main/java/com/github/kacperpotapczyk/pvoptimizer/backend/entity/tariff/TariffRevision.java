package com.github.kacperpotapczyk.pvoptimizer.backend.entity.tariff;

import com.github.kacperpotapczyk.pvoptimizer.backend.entity.Revision;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(
        name = "tariff_revision",
        uniqueConstraints = {@UniqueConstraint(name = "uc_tariff_revision_number", columnNames = {"base_id", "revision_number"})}
)
public class TariffRevision extends Revision {

    @ManyToOne
    @JoinColumn(name = "base_id")
    private Tariff tariff;

    @Column(name = "default_price", nullable = false, columnDefinition = "float(53) default 0.0")
    private double defaultPrice;

    public TariffRevision(Long revisionNumber, double defaultPrice) {
        super(revisionNumber);
        this.defaultPrice = defaultPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TariffRevision that)) return false;
        if (!super.equals(o)) return false;

        return getTariff() != null ? getTariff().equals(that.getTariff()) : that.getTariff() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getTariff() != null ? getTariff().hashCode() : 0);
        return result;
    }

    @Override
    public void softDelete() {
        setDeleted(true);
    }
}