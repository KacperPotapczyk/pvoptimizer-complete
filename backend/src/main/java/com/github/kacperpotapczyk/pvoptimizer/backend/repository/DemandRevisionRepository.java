package com.github.kacperpotapczyk.pvoptimizer.backend.repository;

import com.github.kacperpotapczyk.pvoptimizer.backend.entity.demand.DemandRevision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DemandRevisionRepository extends JpaRepository<DemandRevision, Long> {

    @Query("select r from DemandRevision r " +
            "where r.demand.name = :name and r.demand.isDeleted = false " +
            "and r.revisionNumber = :revisionNumber and r.isDeleted = false")
    Optional<DemandRevision> findByBase_NameAndRevisionNumber(String name, long revisionNumber);
}