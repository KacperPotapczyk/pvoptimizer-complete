package com.github.kacperpotapczyk.pvoptimizer.backend.entity.production;

import com.github.kacperpotapczyk.pvoptimizer.backend.entity.Revision;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Table(
        name = "production_revision",
        uniqueConstraints = {@UniqueConstraint(name = "uc_production_revision_number", columnNames = {"base_id", "revision_number"})}
)
public class ProductionRevision extends Revision {

    @ManyToOne
    @JoinColumn(name = "base_id")
    private Production production;

    @OneToMany(mappedBy = "productionRevision", cascade = CascadeType.ALL)
    private Set<ProductionValue> productionValues = new HashSet<>();

    public Set<ProductionValue> getProductionValuesForTimeWindow(LocalDateTime windowStart, LocalDateTime windowEnd) {

        Set<ProductionValue> productionValueSet = new HashSet<>();

        Optional<ProductionValue> windowStartProductionValue = productionValues.stream()
                .filter(productionValue -> productionValue.getDateTime().isEqual(windowStart))
                .findAny();

        if (windowStartProductionValue.isPresent()) {
            productionValueSet.add(windowStartProductionValue.get());
        } else {
            Optional<ProductionValue> productionValueBeforeWindowStart = productionValues.stream()
                    .filter(productionValue -> productionValue.getDateTime().isBefore(windowStart))
                    .max(Comparator.comparing(ProductionValue::getDateTime));
            productionValueBeforeWindowStart.ifPresent(productionValueSet::add);
        }

        productionValueSet.addAll(productionValues.stream()
                .filter(productionValue -> productionValue.isActiveInTimeWindow(windowStart, windowEnd))
                .collect(Collectors.toSet())
        );

        return productionValueSet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductionRevision that)) return false;
        if (!super.equals(o)) return false;

        return getProduction() != null ? getProduction().equals(that.getProduction()) : that.getProduction() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getProduction() != null ? getProduction().hashCode() : 0);
        return result;
    }

    @Override
    public void softDelete() {
        setDeleted(true);
    }
}