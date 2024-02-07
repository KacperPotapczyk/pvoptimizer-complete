package com.github.kacperpotapczyk.pvoptimizer.backend.repository;

import com.github.kacperpotapczyk.pvoptimizer.backend.entity.production.ProductionRevision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProductionRevisionRepository extends JpaRepository<ProductionRevision, Long> {

    @Query("select r from ProductionRevision r " +
            "where r.production.name = :name and r.production.isDeleted = false " +
            "and r.revisionNumber = :revisionNumber and r.isDeleted = false")
    Optional<ProductionRevision> findByBase_NameAndRevisionNumber(String name, long revisionNumber);
}
